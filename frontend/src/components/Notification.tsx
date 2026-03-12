import { useEffect } from "react";

type NotificationVariant = "error" | "success" | "info";

interface NotificationProps {
    message: string;
    variant?: NotificationVariant;
    onDismiss?: () => void;
    autoDismissMs?: number;
}

function Notification({ message, variant = "info", onDismiss, autoDismissMs = 4000 }: NotificationProps) {
    useEffect(() => {
        if (!onDismiss) return;
        const timer = setTimeout(onDismiss, autoDismissMs);
        return () => clearTimeout(timer);
    }, [onDismiss, autoDismissMs]);

    return (
        <div className={`notification notification--${variant}`}>
            <span className="notification__message">{message}</span>
            {onDismiss && (
                <button className="notification__close" onClick={onDismiss} aria-label="Dismiss">
                    &times;
                </button>
            )}
        </div>
    );
}

export default Notification;
