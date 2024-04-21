import {Component} from "react";
import "../home/Greetings.css"

class Greetings extends Component {
    render() {
        return (
            <div className={"greeting-form"}>
                <div className={"phrase-1"}>
                    IMAGINE THAT..
                </div>
                <div className={"phrase-2"}>
                    ...a social network where you are the main character. Discover new opportunities with us! Become part of a community that changes the world
                </div>
                <div className={"greetings-buttons"}>
                    <button className={"button-1"}>Sign In</button>
                    <button className={"button-2"}>Sign Up</button>
                </div>
            </div>
        );
    }
}

export default Greetings;