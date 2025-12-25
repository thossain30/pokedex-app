import { useEffect, useRef, useState } from "react";
import './CryButton.css';
import { PokemonDetails } from "../../config/Helpers";
import CircularProgress from '@mui/material/CircularProgress';

const CryButton = (pokemon: PokemonDetails | null) => {
    const audioRef = useRef<HTMLAudioElement | null>(null);
    const [progress, setProgress] = useState(0);
    const isPlaying = progress > 0 && progress < 100;
    const playButton = require('../../assets/play_button.png');
    const pauseButton = require('../../assets/pause_button.png');

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
        <div className="buttonContainer">
            <CircularProgress variant="determinate" 
                value={progress} 
                size="13vw"
                sx={{ color: "#fb7fc9ff" }}
                />
            <button className="centerIcon" onClick={playCry} disabled={!pokemon?.cry}>
                {isPlaying ? <img src={pauseButton} /> : <img src={playButton} />}
            </button>
        </div>
    )
}

export default CryButton;