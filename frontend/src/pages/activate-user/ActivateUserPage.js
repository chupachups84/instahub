import {useEffect, useState} from "react";
import {activate} from "../../store/instahub/components/authentication/actions/authenticationActionsCreator";
import {useNavigate} from "react-router-dom";
import {Dispatch} from "redux";
import {useDispatch} from "react-redux";

//todo -> стили )))
const ActivateUserPage = () => {
    const delay = ms => new Promise(res => setTimeout(res, ms));
    const navigate = useNavigate();
    const dispatch: Dispatch = useDispatch();

    const [success, setSuccess] = useState(false);
    const [username, setUsername] = useState('');

    useEffect(() => {
        const queryParams = new URLSearchParams(window.location.search);
        const token = queryParams.get('token');

        if (token) {
            setSuccess(true);
            dispatch(activate(token))
                .catch(() => {
                    setSuccess(false);
                });
            const storedData = localStorage.getItem('userData');
            if (storedData) {
                const parsedData = JSON.parse(storedData);
                setUsername(parsedData.username);
            }
            console.log('Token:', token);
        } else {
            console.log('Token not found');
        }
    }, []);

    return (
        <>
            {success && username? (
                <div>
                    <h1>User successfully activated</h1>
                    <button
                        onClick={()=>navigate('/' + username)}
                    >
                        navigate to userPage
                    </button>
                </div>

            ) : (
                <div>
                    <h1>Something went wrong</h1>
                    <button
                        onClick={() => navigate('/sign-up')}
                    >
                        navigate to signUpPage
                    </button>
                </div>
            )}
        </>
    )
}
export default ActivateUserPage;