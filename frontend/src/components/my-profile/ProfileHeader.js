import {loadUserData} from "../../store/instahub/components/users/actions/userDataActionsCreator";
import {useDispatch} from "react-redux";
import {useEffect, useState} from "react";
import './ProfileHeader.css'
import '../../pages/my-profile/ProfilePage.css'

const ProfileHeader = (name) => {
    const dispatch = useDispatch();

    const [dataLoaded, setDataLoaded] = useState(false);
    const [username, setUsername] = useState('');
    const [firstName, setFirstname] = useState('');
    const [lastName, setLastname] = useState('');
    const [bio, setBio] = useState('');
    const [followers, setFollowers] = useState(-1);
    const [follows, setFollows] = useState(-1);

    useEffect(() => {
        // Загрузка данных пользователя при монтировании компонента
        dispatch(loadUserData(name))
            .then(() => {
                // Установите dataLoaded в true, когда данные успешно загружены
                setDataLoaded(true);
            });
    }, [dispatch, name]);

    useEffect(() => {
        if (dataLoaded) {
            const userData = JSON.parse(localStorage.getItem('userData'));
            setUsername(userData.username);
            setFirstname(userData.firstName);
            setLastname(userData.lastName);
            setBio(userData.bio);
            setFollowers(userData.followersCount);
            setFollows(userData.followsCount);
        }
    }, [dataLoaded]);

    return (
        <>
            <div>
                <div className="container">
                    <div className="profile">
                        <div className="profile-image">
                            <img
                                src="https://sun1-98.userapi.com/impg/7D-NMJxJLdFOKgXYQGUUnbGYTkUvYXL8MGDsAA/_m4bXZZYoek.jpg?size=719x679&quality=96&sign=55c8fef48b6612c1f884d55888b731d2&type=album"
                                alt=""/>
                        </div>
                        <div className="profile-user-settings">
                            <h1 className="profile-user-name">{dataLoaded ? username : 'loading...'}</h1>
                            <button className="profile-edit-btn">Edit Profile</button>
                        </div>
                        <div className="profile-stats">
                            <ul>
                                <li><span className="profile-stat-count">164</span> posts</li>
                                <li><span
                                    className="profile-stat-count">{dataLoaded ? followers : 'loading...'}</span> followers
                                </li>
                                <li><span
                                    className="profile-stat-count">{dataLoaded ? follows : 'loading...'}</span> following
                                </li>
                            </ul>
                        </div>
                        <div className="profile-bio">
                            <p><span
                                className="profile-real-name">{dataLoaded ? lastName + ' ' + firstName + ' ' : 'loading...'}</span>{dataLoaded ? bio : 'loading...'}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default ProfileHeader;
