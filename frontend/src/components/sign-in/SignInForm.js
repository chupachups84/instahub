import '../../components/sign-in/SignInForm.css'
import axios from "axios";
import RouteNames from "../../router/routes";
import {useNavigate} from "react-router-dom";
import {useRef, useState} from "react";

const LOGIN_URL = 'http://localhost:8080/api/v1/auth/login'

const SignInForm = () => {
    const navigate = useNavigate();

    const userRef = useRef();
    const errRef = useRef();

    //username
    const [username, setUsername] = useState('');

    //password
    const [password, setPassword] = useState('');

    const [errMsg, setErrMsg] = useState('');
    const [success, setSuccess] = useState(false);

    const handleSubmit = async (e) => {
        //отправляем запрос в REST API
        e.preventDefault();

        try {
            const response = await axios.post(LOGIN_URL,
                JSON.stringify({
                    username,
                    password,
                }),
                {
                    headers: {'Content-Type': 'application/json'},
                    withCredentials: false
                }
            );
            console.log(response?.access_token);
            console.log(response?.refresh_token);
            setSuccess(true);
            setUsername('');
            setPassword('');
        } catch (err) {
            if (!err?.response) {
                setErrMsg('No server response');
            } else {
                setErrMsg('Bad password or username. Please try again')
            }
            errRef.current.focus();
        }
        console.log('user send data')
    }

    return (
        <>
            {success ? (
                navigate(RouteNames.FEED)
            ) : (
                <section className={'sign-in__form'}>
                    <div className={"create-account"}>Welcome back</div>
                    <div className={"greetings"}>Sign in to see your friends' photos!</div>
                    <p
                        ref={errRef}
                        className={errMsg ? "errmsg" : "hide"}
                        aria-live="assertive">{errMsg}
                    </p>
                    <form onSubmit={handleSubmit}>

                        <input
                            className={"input-user"}
                            type="text"
                            id="username"
                            placeholder={"Имя пользователя"}
                            ref={userRef}
                            autoComplete="off"
                            onChange={(e) => setUsername(e.target.value)}
                            value={username}
                            required
                            aria-describedby="uidnote"
                        />

                        <input
                            className={"input-user"}
                            type="password"
                            id="password"
                            placeholder={"Пароль"}
                            onChange={(e) => setPassword(e.target.value)}
                            value={password}
                            required
                            aria-describedby="pwdnote"
                        />
                        <button className={'button'}>
                            Sign Up
                        </button>
                    </form>
                </section>
            )}
        </>
    )
}

export default SignInForm


// import React, {Component} from "react";
// import '../sign-in/SignInForm.css'
//
// class SignInForm extends Component {
//     render() {
//         return (
//             <div className={"sign-in__form"}>
//                 <div className={"create-account"}>Welcome back</div>
//                 <div className={"greetings"}>Sign in to see your friends photos!</div>
//                 <input type={"text"} className={"input-user"} placeholder={"Enter your username"}/>
//                 <input type={"password"} className={"input-user"} placeholder={"Enter your password"}/>
//                 <button className={"button"}>Sign In</button>
//             </div>
//         );
//     }
// }
//
// export default SignInForm;