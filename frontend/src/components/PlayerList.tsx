function PlayerList({ players }: { players: string[] }) {
    return (
        <div className="player-list">
            <h3 className="player-list__title">
                Players Joined ({players.length})
            </h3>
            {players.length === 0 ? (
                <p className="text-muted">No players yet</p>
            ) : (
                <ul className="player-list__items">
                    {players.map((name, idx) => (
                        <li key={idx} className="player-list__item">
                            <span className="player-list__avatar">
                                {name.charAt(0).toUpperCase()}
                            </span>
                            <span>{name}</span>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default PlayerList;
