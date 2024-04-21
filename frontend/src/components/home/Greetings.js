import {Component} from "react";
import "../home/Greetings.css"
import {Link} from "react-router-dom";
import RouteNames from "../../router/routes";

class Greetings extends Component {
    render() {
        return (
            <div className={"greeting-form"}>
                <div className={"greetings-phrase-1"}>
                    IMAGINE THAT..
                </div>
                <div className={"greetings-phrase-2"}>
                    ...a social network where you are the main character. Discover new opportunities with us! Become part of a community that changes the world
                </div>
                <div className={"greetings-buttons"}>
                    <Link to={RouteNames.SIGN_IN}>
                        <button className={"greetings-button-1"}>Sign In</button>
                    </Link>
                    <Link to={RouteNames.SIGN_UP}>
                        <button className={"greetings-button-2"}>Sign Up</button>
                    </Link>
                </div>
            </div>
        );
    }
}

export default Greetings;