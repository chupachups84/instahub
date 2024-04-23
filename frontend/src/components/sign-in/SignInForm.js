import React, {Component} from "react";
import '../sign-in/SignInForm.css'

class SignInForm extends Component {
    render() {
        return (
            <div className={"sign-in__form"}>
                <div className={"create-account"}>Welcome back</div>
                <div className={"greetings"}>Sign in to see your friends photos!</div>
                <input type={"text"} className={"input-user"} placeholder={"Enter your username"}/>
                <input type={"password"} className={"input-user"} placeholder={"Enter your password"}/>
                <button className={"button"}>Sign In</button>
            </div>
        );
    }
}

export default SignInForm;