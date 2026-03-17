function LoadingSpinner({ message }: { message: string }) {
    return (
        <div className="loading-spinner">
            <div className="loading-spinner__spinner" />
            <p className="loading-spinner__message">{message}</p>
        </div>
    );
}

export default LoadingSpinner;