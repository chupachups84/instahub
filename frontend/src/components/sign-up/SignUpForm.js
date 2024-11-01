import '../../components/sign-up/SignUpForm.css'
import {useEffect, useRef, useState} from "react";
import axios from "axios";
import RouteNames from "../../router/routes";
import {Link} from "react-router-dom";


const NAME_REGEX = /^[а-яА-Яa-zA-ZЁёәіңғүұқөһӘІҢҒҮҰҚӨҺ\-\s]*$/;
const USER_REGEX = /^[A-z][A-z0-9-_]{3,23}$/;
const PWD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%+\\/\-=]).{8,24}$/;
const EMAIL_REGEX = /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/;
const REGISTER_URL = '/api/v1/auth/register'

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

    //bio
    const [bio, setBio] = useState('');

    //patronymic
    const [patronymic, setPatronymic] = useState('');

    //email
    const [email, setEmail] = useState('');
    const [validEmail, setValidEmail] = useState(false);
    const [emailFocus, setEmailFocus] = useState(false);

    //birthDate
    const [birthDate,setBirthDate] = useState(new Date());
    const [validBirthDate,setValidBirthDate] = useState(false);
    const [birthDateFocus,setBirthDateFocus] = useState(false);

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
    const [successMessage,setSuccessMessage] = useState('');

    useEffect(() => {
        userRef.current.focus();
    }, [])

    useEffect(() => {
        setValidEmail(EMAIL_REGEX.test(email));
    }, [email])

    useEffect(() => {
        setValidFName(NAME_REGEX.test(first_name));
    }, [first_name])

    useEffect(() => {
        setValidLName(NAME_REGEX.test(last_name));
    }, [last_name])

    useEffect(() => {
        setValidUsername(USER_REGEX.test(username));
    }, [username])

    useEffect(() => {
        setValidPwd(PWD_REGEX.test(password));
        setValidMatch(password === matchPwd);
    }, [password, matchPwd])

    useEffect(() => {
        const currentDate = new Date();
        var age = currentDate.getFullYear() - birthDate.getFullYear();
        const m = currentDate.getMonth() - birthDate.getMonth();
        if (m < 0 || (m === 0 && currentDate.getDate() < birthDate.getDate())) {
            age--;
        }
        setValidBirthDate(age>=18);
    }, [birthDate])

    useEffect(() => {
        setErrMsg('');
    }, [username, email, password, matchPwd,birthDate])

    const handleSubmit = async (e) => {

        e.preventDefault();

        ///можно убрать как-то наверное, для валидации возраста
        const currentDate = new Date();
        var age = currentDate.getFullYear() - birthDate.getFullYear();
        const m = currentDate.getMonth() - birthDate.getMonth();
        if (m < 0 || (m === 0 && currentDate.getDate() < birthDate.getDate())) {
            age--;
        }
        ///

        if (
            !USER_REGEX.test(username) ||
            !PWD_REGEX.test(password) ||
            !EMAIL_REGEX.test(email) ||
            last_name.trim() === "" ||
            first_name.trim() === ""||
            age<18
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
                    patronymic,
                    bio,
                    birthDate
                }),
                {
                    headers: {'Content-Type': 'application/json'},
                    withCredentials: false
                }
            );
            setSuccess(true);
            setSuccessMessage(response.data)
            setUsername('');
            setPassword('');
            setMatchPwd('');
        } catch (err) {
            if (!err?.response) {
                setErrMsg('No Server Response');
            } else if (err.response?.status === 400) {
                //todo-> поработать с парсингом ошибок
                setErrMsg(err.response.data.errorsMessage[0]);
            } else {
                setErrMsg('Registration Failed')
            }
            errRef.current.focus();
        }
    }

    const handleBirthDateChange = (event) => {
        const value = event.target.value;
        if (value) {
            setBirthDate(new Date(value));
        } else {
            setBirthDate(new Date());
        }
    };

    return (
        <>
            {success ? (
                <section className={'sign-up__form'}>
                    <div className={"greetings"}>{successMessage}</div>
                </section>
            ) : (
                <section className={'sign-up__form'}>
                <div className={"create-account"}>Create account</div>
                    <div className={"greetings"}>Share your thoughts with the world from today</div>
                    <p
                        ref={errRef}
                        className={errMsg ? "errmsg" : "hide"}
                    >
                        {errMsg}
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
                            Invalid email format: example@email.com
                        </p>

                        <input
                            className={"input-user"}
                            type="date"
                            id="birthDate"
                            placeholder={""}
                            ref={userRef}
                            autoComplete="off"
                            onChange={handleBirthDateChange}
                            value={birthDate.toISOString().split('T')[0]}
                            required
                            aria-invalid={validBirthDate ? "false" : "true"}
                            aria-describedby="uidnote"
                            onFocus={() => setBirthDateFocus(true)}
                            onBlur={() => setBirthDateFocus(false)}
                        />
                        <p id="uidnote"
                           className={birthDateFocus && birthDate && !validBirthDate ? "instructions" : "hide"}>
                            You're not old enough yet: lower 18 years
                        </p>

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
                            type="text"
                            id="bio"
                            placeholder={"Био"}
                            ref={userRef}
                            autoComplete="off"
                            onChange={(e) => setBio(e.target.value)}
                            value={bio}
                        />


                        <button
                            disabled={
                                !validUsername ||
                                !validFName ||
                                !validLName ||
                                !validEmail ||
                                !validPwd ||
                                !validMatch ||
                                !validBirthDate
                            }
                            className={'button'}>Sign Up
                        </button>
                    </form>
                    <p>
                        Already registered?<br/>
                        <span className="line">
                            <Link to={RouteNames.SIGN_IN}>
                                <p>Sign In</p>
                            </Link>
                        </span>
                    </p>
                </section>
            )}
        </>
    )
}

export default SignUpForm