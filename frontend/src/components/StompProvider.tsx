import { createContext, useCallback, useContext, useRef, useState, type ReactNode } from "react";
import { Client, type IMessage } from "@stomp/stompjs";
import { WS_BASE_URL } from "../config";

interface StompContextValue {
  connected: boolean;
  connect: () => void;
  disconnect: () => void;
  subscribe: (destination: string, callback: (msg: IMessage) => void) => (() => void) | undefined;
  publish: (destination: string, body: unknown) => void;
}

const StompContext = createContext<StompContextValue | null>(null);

const MAX_RETRIES = 5;
const RETRY_DELAY_MS = 2000;

export function useStomp() {
  const ctx = useContext(StompContext);
  if (!ctx) throw new Error("useStomp must be used within <StompProvider>");
  return ctx;
}

export default function StompProvider({ children }: { children: ReactNode }) {
  const clientRef = useRef<Client | null>(null);
  const retryCountRef = useRef(0);
  const retryTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const hasEverConnectedRef = useRef(false);
  const [connected, setConnected] = useState(false);

  const connect = useCallback(() => {
    if (clientRef.current?.active) return;

    const wsUrl = WS_BASE_URL.replace(/^http/, "ws") + "/ws-aha";
    const client = new Client({
      brokerURL: wsUrl,
      reconnectDelay: 0,
      onConnect: () => {
        retryCountRef.current = 0;
        hasEverConnectedRef.current = true;
        setConnected(true);
      },
      onDisconnect: () => setConnected(false),
      onStompError: (frame) => {
        console.error("STOMP error:", frame.headers["message"], frame.body);
        if (!hasEverConnectedRef.current) tryRetry();
      },
      onWebSocketClose: () => {
        if (hasEverConnectedRef.current) return;
        tryRetry();
      },
      onWebSocketError: () => {
        if (hasEverConnectedRef.current) return;
        tryRetry();
      },
    });

    const tryRetry = () => {
      if (retryTimeoutRef.current) return;
      if (retryCountRef.current >= MAX_RETRIES) {
        console.warn(`STOMP: gave up after ${MAX_RETRIES} connection attempts`);
        return;
      }
      retryCountRef.current += 1;
      retryTimeoutRef.current = setTimeout(() => {
        retryTimeoutRef.current = null;
        connect();
      }, RETRY_DELAY_MS);
      client.deactivate();
      clientRef.current = null;
    };

    client.activate();
    clientRef.current = client;
  }, []);

  const disconnect = useCallback(() => {
    if (retryTimeoutRef.current) {
      clearTimeout(retryTimeoutRef.current);
      retryTimeoutRef.current = null;
    }
    retryCountRef.current = 0;
    hasEverConnectedRef.current = false;
    clientRef.current?.deactivate();
    clientRef.current = null;
    setConnected(false);
  }, []);

  const subscribe = useCallback((destination: string, callback: (msg: IMessage) => void) => {
    const client = clientRef.current;
    if (!client?.connected) return undefined;
    const sub = client.subscribe(destination, callback);
    return () => sub.unsubscribe();
  }, []);

  const publish = useCallback((destination: string, body: unknown) => {
    const client = clientRef.current;
    if (!client?.connected) return;
    client.publish({ destination, body: JSON.stringify(body) });
  }, []);

  return (
    <StompContext.Provider value={{ connected, connect, disconnect, subscribe, publish }}>
      {children}
    </StompContext.Provider>
  );
}
