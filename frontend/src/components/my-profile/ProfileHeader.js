import {loadUserData} from "../../store/instahub/components/users/actions/userDataActionsCreator";
import {useDispatch} from "react-redux";
import {useEffect, useState} from "react";
import './ProfileHeader.css'
import '../../pages/my-profile/ProfilePage.css'
import Modal from "../modal/Modal";
import {
    fetchFollowers,
    fetchFollows
} from "../../store/instahub/components/subscription/actions/subscriptionActionsCreator";
import SubscriptionList from "../subscription/SubscriptionList";

const ProfileHeader = (name) => {
    const dispatch = useDispatch();

    const [dataLoaded, setDataLoaded] = useState(false);
    const [username, setUsername] = useState('');
    const [firstName, setFirstname] = useState('');
    const [lastName, setLastname] = useState('');
    const [bio, setBio] = useState('');
    const [followersCount, setFollowersCount] = useState(-1);
    const [followsCount, setFollowsCount] = useState(-1);

    //subscription part
    const [followersModalActive, setFollowersModalActive] = useState(false);
    const [followsModalActive, setFollowsModalActive] = useState(false);
    const [followersLoading, setFollowersLoading] = useState(true);
    const [followsLoading, setFollowsLoading] = useState(true);

    const [followers, setFollowers] = useState(
        JSON.parse(localStorage.getItem("followers")) === null ?
            [] : JSON.parse(localStorage.getItem("followers"))
    );
    const [follows, setFollows] = useState(
        JSON.parse(localStorage.getItem("follows")) === null ?
            [] : JSON.parse(localStorage.getItem("follows"))
    );

    let size = 5;

    const [nextFollowersPage, setNextFollowersPage] = useState(0);
    const [nextFollowsPage, setNextFollowsPage] = useState(0);


    const fetchFollowersNextPage = (page) => {
        console.log("follower page = " + page)
        setFollowersLoading(true)
        if(followersCount>(size * page)) {
            dispatch(fetchFollowers(page, size, username))
                .then(
                    () => {
                        setFollowers(
                            JSON.parse(localStorage.getItem("followers")) === null ?
                                [] : JSON.parse(localStorage.getItem("followers"))
                        );
                        setFollowersLoading(false)
                    }
                );
            setNextFollowersPage(page + 1);
        }
        else {
            setFollowersLoading(false);
        }
    }

    const fetchFollowsNextPage = (page) => {
        console.log("follows page = " + page)
        setFollowsLoading(true);
        if(followsCount>(size * page)) {
            dispatch(fetchFollows(page, size, username))
                .then(
                    () => {
                        setFollows(
                            JSON.parse(localStorage.getItem("follows")) === null ?
                                [] : JSON.parse(localStorage.getItem("follows"))
                        )
                        setFollowsLoading(false);
                    }
                );
            setNextFollowsPage(page + 1);
        }
        else {
            setFollowsLoading(false);
        }
    }

    useEffect(() => {
        setDataLoaded(false);
        dispatch(loadUserData(name))
            .then(() => {
                setDataLoaded(true);
                setNextFollowersPage(0)
                setNextFollowsPage(0)
            });
    }, [dispatch, name]);

    useEffect(() => {
        if (dataLoaded) {
            const userData = JSON.parse(localStorage.getItem('userData'));
            setUsername(userData.username);
            setFirstname(userData.firstName);
            setLastname(userData.lastName);
            setBio(userData.bio);
            setFollowersCount(userData.followersCount);
            setFollowsCount(userData.followsCount);
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
                                <li onClick={() => {
                                    setFollowersModalActive(true);
                                    fetchFollowersNextPage(nextFollowersPage);
                                }}><span
                                    className="profile-stat-count">{dataLoaded ? followersCount : 'loading...'}</span> followers
                                </li>
                                <li onClick={() => {
                                    setFollowsModalActive(true)
                                    fetchFollowsNextPage(nextFollowsPage);
                                }}><span
                                    className="profile-stat-count">{dataLoaded ? followsCount : 'loading...'}</span> following
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

            <Modal
                active={followersModalActive}
                setActive={setFollowersModalActive}
                children={
                <SubscriptionList
                    subscriptions={followersLoading?[]:followers}
                    action={{
                        fetchFunction: fetchFollowersNextPage,
                        nextPage: nextFollowersPage
                    }}
                    modalSetter={{
                        setActive: setFollowersModalActive
                    }}
                />}
            />
            <Modal
                active={followsModalActive}
                setActive={setFollowsModalActive}
                children={
                <SubscriptionList
                    subscriptions={followsLoading?[]:follows}
                    action={{
                        fetchFunction: fetchFollowsNextPage,
                        nextPage: nextFollowsPage
                    }}
                    modalSetter={{
                        setActive: setFollowsModalActive
                    }}
                />}
            />
        </>
    );
};

export default ProfileHeader;
