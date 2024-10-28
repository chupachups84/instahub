import '../sign-up/SignUpPage.css';
import React, {Component} from "react";
import Logo from "../../components/Logo";
import SignUpImage from "../../components/sign-up/SignUpImage";
import SignUpForm from "../../components/sign-up/SignUpForm";

class SignUpPage extends Component {

    render() {
        return (
            <div className={"sign-up"}>
                <Logo/>
                <SignUpImage/>
                <SignUpForm/>
            </div>
        );
    }
}

export default SignUpPage;