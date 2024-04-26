import '../../components/sign-up/SignUpForm.css'
import {useEffect, useRef, useState} from "react";
import axios from "axios";
import RouteNames from "../../router/routes";
import {Link} from "react-router-dom";

const USER_REGEX = /^[A-z][A-z0-9-_]{3,23}$/;
const PWD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%+\\\/\-=]).{8,24}$/;
const EMAIL_REGEX = /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/;
const REGISTER_URL = 'http://localhost:8080/api/v1/auth/register'

const SignUpForm = () => {
    const userRef = useRef();
    const errRef = useRef();

    //first_name
    const [first_name, setFirstname] = useState('');
    const [validFName, setValidFName] = useState(false);
    const [fNameFocus, setFNameFocus] = useState(false);

    //last_name
    const [last_name, setLastname] = useState('');
    const [validLName, setValidLName] = useState(false);
    const [lNameFocus, setLNameFocus] = useState(false);

    //middle_name
    const [middle_name, setMiddlename] = useState('');

    //patronymic
    const [patronymic, setPatronymic] = useState('');

    //email
    const [email, setEmail] = useState('');
    const [validEmail, setValidEmail] = useState(false);
    const [emailFocus, setEmailFocus] = useState(false);
    //username
    const [username, setUsername] = useState('');
    const [validUsername, setValidUsername] = useState(false);
    const [usernameFocus, setUsernameFocus] = useState(false);
    //password
    const [password, setPassword] = useState('');
    const [validPwd, setValidPwd] = useState(false);
    const [pwdFocus, setPwdFocus] = useState(false);
    //password2
    const [matchPwd, setMatchPwd] = useState('');
    const [validMatch, setValidMatch] = useState(false);
    const [matchFocus, setMatchFocus] = useState(false);

    const [errMsg, setErrMsg] = useState('');
    const [success, setSuccess] = useState(false);

    useEffect(() => {
        userRef.current.focus();
    }, [])

    useEffect(() => {
        setValidEmail(EMAIL_REGEX.test(email));
    }, [email])

    useEffect(() => {
        setValidFName(first_name.trim() !== "");
    }, [first_name])

    useEffect(() => {
        setValidLName(last_name.trim() !== "");
    }, [last_name])

    useEffect(() => {
        setValidUsername(USER_REGEX.test(username));
    }, [username])

    useEffect(() => {
        setValidPwd(PWD_REGEX.test(password));
        setValidMatch(password === matchPwd);
    }, [password, matchPwd])

    useEffect(() => {
        setErrMsg('');
    }, [username, email, password, matchPwd])

    const handleSubmit = async (e) => {
        //тут отправляем запрос в rest api можно что-то подшаманить, парсить ошибки , записать токены в стор и тд
        e.preventDefault();
        // if button enabled with JS hack
        if (
            !USER_REGEX.test(username) ||
            !PWD_REGEX.test(password) ||
            !EMAIL_REGEX.test(email) ||
            last_name.trim() === "" ||
            first_name.trim() === ""
        ) {
            setErrMsg("Invalid Entry");
            return;
        }
        try {
            const response = await axios
                .post(REGISTER_URL,
                JSON.stringify({
                    username,
                    password,
                    email,
                    first_name,
                    last_name,
                    middle_name,
                    patronymic
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
            setMatchPwd('');
        } catch (err) {
            if (!err?.response) {
                setErrMsg('No Server Response');
            } else if (err.response?.status === 409) {
                setErrMsg('Username Taken');
            } else {
                setErrMsg('Registration Failed')
            }
            errRef.current.focus();
        }
        console.log('user send data')
    }

    return (
        <>
            {success ? (
                <section className={'sign-up__form'} >
                    <h1>Success!</h1>
                    <Link to={RouteNames.SIGN_IN}>
                        <button className={"greetings-button-1"}>Sign In</button>
                    </Link>
                </section>
            ) : (
                <section className={'sign-up__form'}>
                    <div className={"create-account"}>Create account</div>
                    <div className={"greetings"}>Share your thoughts with the world from today</div>
                    <p
                        ref={errRef}
                        className={errMsg ? "errmsg" : "hide"}
                        aria-live="assertive"
                    >
                        {errMsg}
                    </p>
                    <form onSubmit={handleSubmit}>

                        <input
                            className={"input-user"}
                            type="text"
                            id="last_name"
                            placeholder={"Фамилия"}
                            ref={userRef}
                            autoComplete="off"
                            onChange={(e) => setLastname(e.target.value)}
                            value={last_name}
                            required
                            aria-invalid={validLName ? "false" : "true"}
                            aria-describedby="uidnote"
                            onFocus={() => setLNameFocus(true)}
                            onBlur={() => setLNameFocus(false)}
                        />
                        <p id="uidnote" className={lNameFocus && last_name && !validLName ? "instructions" : "hide"}>
                            Invalid last name
                        </p>

                        <input
                            className={"input-user"}
                            type="text"
                            id="first_name"
                            placeholder={"Имя"}
                            ref={userRef}
                            autoComplete="off"
                            onChange={(e) => setFirstname(e.target.value)}
                            value={first_name}
                            required
                            aria-invalid={validFName ? "false" : "true"}
                            aria-describedby="uidnote"
                            onFocus={() => setFNameFocus(true)}
                            onBlur={() => setFNameFocus(false)}
                        />
                        <p id="uidnote" className={fNameFocus && first_name && !validFName ? "instructions" : "hide"}>
                            Invalid first name
                        </p>

                        <input
                            className={"input-user"}
                            type="text"
                            id="middle_name"
                            placeholder={"Псевдоним"}
                            ref={userRef}
                            autoComplete="off"
                            onChange={(e) => setMiddlename(e.target.value)}
                            value={middle_name}
                        />

                        <input
                            className={"input-user"}
                            type="text"
                            id="patronymic"
                            placeholder={"Отчество"}
                            ref={userRef}
                            autoComplete="off"
                            onChange={(e) => setPatronymic(e.target.value)}
                            value={patronymic}
                        />

                        <input
                            className={"input-user"}
                            type="email"
                            id="email"
                            placeholder={"Почта"}
                            ref={userRef}
                            autoComplete="off"
                            onChange={(e) => setEmail(e.target.value)}
                            value={email}
                            required
                            aria-invalid={validEmail ? "false" : "true"}
                            aria-describedby="uidnote"
                            onFocus={() => setEmailFocus(true)}
                            onBlur={() => setEmailFocus(false)}
                        />
                        <p id="uidnote" className={emailFocus && email && !validEmail ? "instructions" : "hide"}>
                            Invalid email
                        </p>

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
                            aria-invalid={validUsername ? "false" : "true"}
                            aria-describedby="uidnote"
                            onFocus={() => setUsernameFocus(true)}
                            onBlur={() => setUsernameFocus(false)}
                        />
                        <p id="uidnote"
                           className={usernameFocus && username && !validUsername ? "instructions" : "hide"}>
                            4 to 24 characters.<br/>
                            Must begin with a letter.<br/>
                            Letters, numbers, underscores, hyphens allowed.
                        </p>

                        <input
                            className={"input-user"}
                            type="password"
                            id="password"
                            placeholder={"Пароль"}
                            onChange={(e) => setPassword(e.target.value)}
                            value={password}
                            required
                            aria-invalid={validPwd ? "false" : "true"}
                            aria-describedby="pwdnote"
                            onFocus={() => setPwdFocus(true)}
                            onBlur={() => setPwdFocus(false)}
                        />
                        <p id="pwdnote" className={pwdFocus && !validPwd ? "instructions" : "hide"}>
                            8 to 24 characters.<br/>
                            Must include uppercase and lowercase letters, a number and a special character.<br/>
                            Allowed special characters: <span aria-label="exclamation mark">!</span> <span
                            aria-label="at symbol">@</span> <span aria-label="hashtag">#</span> <span
                            aria-label="dollar sign">$</span> <span aria-label="percent">%</span>
                        </p>

                        <input
                            className={"input-user"}
                            type="password"
                            id="confirm_pwd"
                            placeholder={"Подтверждение пароля"}
                            onChange={(e) => setMatchPwd(e.target.value)}
                            value={matchPwd}
                            required
                            aria-invalid={validMatch ? "false" : "true"}
                            aria-describedby="confirmnote"
                            onFocus={() => setMatchFocus(true)}
                            onBlur={() => setMatchFocus(false)}
                        />
                        <p id="confirmnote" className={matchFocus && !validMatch ? "instructions" : "hide"}>
                            Must match the first password input field.
                        </p>

                        <button
                            disabled={
                                !validUsername ||
                                !validFName ||
                                !validLName ||
                                !validEmail ||
                                !validPwd ||
                                !validMatch
                            }
                            className={'button'}>Sign Up
                        </button>
                    </form>
                    <p>
                        Already registered?<br/>
                        <span className="line">
                            {/*put router link here*/}
                            <a href="#">Sign In</a>
                        </span>
                    </p>
                </section>
            )}
        </>
    )
}

export default SignUpForm