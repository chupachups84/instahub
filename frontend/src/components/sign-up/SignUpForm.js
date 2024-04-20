import React, {Component} from "react";
import '../../components/sign-up/SignUpForm.css'

class SignUpForm extends Component {
    render() {
        return (
            <div className={"sign-up__form"}>
                <div className={"create-account"}>Create account</div>
                <div className={"greetings"}>Share your thoughts with the world from today</div>
                <input type={"text"} className={"input-user"} placeholder={"Enter your name"}/>
                <input type={"email"} className={"input-user"} placeholder={"Enter your email"}/>
                <input type={"text"} className={"input-user"} placeholder={"Create your account name"}/>
                <input type={"password"} className={"input-user"} placeholder={"Create password"}/>
                <input type={"password"} className={"input-user"} placeholder={"Repeat password"}/>
                <button className={"button"}>Sign Up</button>
            </div>
        );
    }
}

export default SignUpForm;