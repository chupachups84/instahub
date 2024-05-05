import '../../components/sign-in/SignInForm.css'
import {useNavigate} from "react-router-dom";
import {useEffect, useRef, useState} from "react";
import {login} from "../../store/instahub/components/authentication/actions/authenticationActionsCreator";
import {useDispatch} from "react-redux";
import {Dispatch} from "redux";


const SignInForm = () => {

    const navigate = useNavigate();

    const dispatch: Dispatch = useDispatch();

    const userRef = useRef();
    const errRef = useRef();

    //username
    const [username, setUsername] = useState('');

    //password
    const [password, setPassword] = useState('');


    const [errMsg, setErrMsg] = useState('');
    const [success, setSuccess] = useState(false);

    const onChangeUsername = (e) => {
        setUsername(e.target.value);
    }

    const onChangePassword = (e) => {
        setPassword(e.target.value);
    }

    useEffect(() => {
        if (success&&username) {
            navigate("/" + username);
        }
    }, [success, username]); // Зависимости хука


    const handleSubmit = async (e) => {
        e.preventDefault()
        try {
            await dispatch(login(username, password))
            setSuccess(true);
        }
        catch (err){
            setErrMsg(err.message)
        }
    }

    return (
        <>
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

        </>
    )
}

export default SignInForm;
