import '../../components/sign-in/SignInForm.css'
import axios from "axios";
import RouteNames from "../../router/routes";
import {useNavigate} from "react-router-dom";
import {useRef, useState} from "react";
import {login} from "../../store/instahub/components/authentication/actions/authenticationActionsCreator";
import {useDispatch} from "react-redux";
import {Dispatch} from "redux";

const LOGIN_URL = 'http://localhost:8080/api/v1/auth/login'

const SignInForm = () => {

    const navigate = useNavigate();

    const dispatch: Dispatch = useDispatch();

    const userRef = useRef();
    const errRef = useRef();

    //username
    const [username, setUsername] = useState('');

    //password
    const [password, setPassword] = useState('');

    //loading
    const [loading, setLoading] = useState(false);

    const [errMsg, setErrMsg] = useState('');
    const [success, setSuccess] = useState(false);

    const onChangeUsername = (e) => {
        setUsername(e.target.value);
    }

    const onChangePassword = (e) => {
        setPassword(e.target.value);
    }

    const handleSubmit = async (e) => {

        //отправляем запрос в REST API
        e.preventDefault();

        setLoading(true);

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
            setSuccess(true);
            setUsername('');
            setPassword('');

            if (response.status.toString().startsWith('2')) {
                dispatch(login(username, password))
                    .catch(() => {
                        setLoading(false);
                    });
            }
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
                            onChange={onChangeUsername}
                            value={username}
                            required
                            aria-describedby="uidnote"
                        />

                        <input
                            className={"input-user"}
                            type="password"
                            id="password"
                            placeholder={"Пароль"}
                            onChange={onChangePassword}
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

export default SignInForm;
