interface ButtonProps {
    type: "button" | "submit";
    onClick?: React.MouseEventHandler<HTMLButtonElement>;
    children: React.ReactNode;
    variant?: "primary" | "secondary" | "success" | "danger" | "ghost";
    size?: "default" | "lg";
    fullWidth?: boolean;
    disabled?: boolean;
}

function Button({
    type,
    onClick,
    children,
    variant = "primary",
    size = "default",
    fullWidth = false,
    disabled = false,
}: ButtonProps) {
    const classes = [
        "btn",
        `btn--${variant}`,
        size === "lg" ? "btn--lg" : "",
        fullWidth ? "btn--full" : "",
    ]
        .filter(Boolean)
        .join(" ");

    return (
        <button type={type} onClick={onClick} className={classes} disabled={disabled}>
            {children}
        </button>
    );
}

export default Button;
