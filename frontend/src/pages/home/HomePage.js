import {Component} from "react";
import Logo from "../../components/Logo";
import "../home/HomePage.css"
import Greetings from "../../components/home/Greetings";

class HomePage extends Component {
    render() {
        return (
            <div className={"home-page-container"}>
                <div className={"home-page"}>
                    <Logo/>
                    <Greetings/>
                </div>
            </div>
        );
    }
}

export default HomePage;