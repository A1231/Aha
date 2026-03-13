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

export function useStomp() {
  const ctx = useContext(StompContext);
  if (!ctx) throw new Error("useStomp must be used within <StompProvider>");
  return ctx;
}

export default function StompProvider({ children }: { children: ReactNode }) {
  const clientRef = useRef<Client | null>(null);
  const [connected, setConnected] = useState(false);

  const connect = useCallback(() => {
    if (clientRef.current?.active) return;

    const wsUrl = WS_BASE_URL.replace(/^http/, "ws") + "/ws-aha";
    const client = new Client({
      brokerURL: wsUrl,
      reconnectDelay: 5000,
      onConnect: () => setConnected(true),
      onDisconnect: () => setConnected(false),
      onStompError: (frame) => {
        console.error("STOMP error:", frame.headers["message"], frame.body);
      },
    });

    client.activate();
    clientRef.current = client;
  }, []);

  const disconnect = useCallback(() => {
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
