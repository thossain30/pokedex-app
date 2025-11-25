import { TypeDto } from "../../config/Helpers";
import './typeSprite.css';

const TypeSprite = (type: TypeDto | null) => {
    if (!type) return null;
    return (
        <img src={type.url} alt={type.name} className="type"/>
    );
}

export default TypeSprite;