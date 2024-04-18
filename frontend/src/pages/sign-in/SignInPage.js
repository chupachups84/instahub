import React, {Component} from "react";
import Logo from "../../components/Logo";
import SignInForm from "../../components/sign-in/SignInForm";
import SignInImage from "../../components/sign-in/SignInImage";
import "../sign-in/SignInPage.css"

class SignInPage extends Component {
    render() {
        return (
            <div className={"sign-in"}>
                <Logo/>
                <SignInImage/>
                <SignInForm/>
            </div>
        );
    }
}

export default SignInPage;