interface ButtonProps {
    type: "button" | "submit" ;

    onClick?: React.MouseEventHandler<HTMLButtonElement>;
    children: React.ReactNode;
}

function Button({ type, onClick, children }: ButtonProps) {
    return (
        <button type={type} onClick={onClick}>{children}</button>
    )
}

export default Button;