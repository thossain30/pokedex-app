import { useEffect, useRef, useState } from "react";
import './CryButton.css';
import { PokemonDetails } from "../../config/Helpers";
import CircularProgress from '@mui/material/CircularProgress';

const CryButton = (pokemon: PokemonDetails | null) => {
    const audioRef = useRef<HTMLAudioElement | null>(null);
    const [progress, setProgress] = useState(0);
    const isPlaying = progress > 0 && progress < 100;

    useEffect(() => {
        if (!pokemon) return;

            const audio = new Audio(pokemon.cry);
            audioRef.current = audio;

            const updateProgress = () => {
                if (!audio.duration) return;
                setProgress((audio.currentTime / audio.duration) * 100);
            }

            audio.addEventListener("timeupdate", updateProgress);
            audio.addEventListener("ended", () => setProgress(0));

            return () => {
                audio.pause();
                audio.removeEventListener("timeupdate", updateProgress);
            };
    }, [pokemon])

    const playCry = () => {
        const audio = audioRef.current;
        if (!audio) return;

        audio.currentTime = 0;
        audio.volume = 0.35;
        audio.play();
    }

    return (
        <button className="cryButton mt-2" onClick={playCry}>
            <div className="progressWrapper">
                <CircularProgress variant="determinate" 
                    value={progress} 
                    size="13vw" 
                    sx={{ color: "#f6aad8"}}
                    />
                <span className="centerIcon">
                    {isPlaying ? "⏸" : "▶"}
                </span>
            </div>
        </button>
    )
}

export default CryButton;