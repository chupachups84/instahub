import {loadUserData} from "../../store/instahub/components/users/actions/userDataActionsCreator";
import {useDispatch} from "react-redux";
import {useEffect, useState} from "react";
import './ProfileHeader.css'
import '../../pages/my-profile/ProfilePage.css'
import {fetchAvatar} from "../../store/instahub/components/profile/actions/profileActionsCreator";
import Modal from "../modal/Modal";
import {
    fetchFollowers,
    fetchFollows
} from "../../store/instahub/components/subscription/actions/subscriptionActionsCreator";
import SubscriptionList from "../subscription/SubscriptionList";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import authHeader from "../../store/instahub/components/authentication/services/authHeader";

const ProfileHeader = (name) => {

    const getUserDataByUsername = (username) => {
        let allUsers = new Map(JSON.parse(localStorage.getItem("userData")));
        if (allUsers === null || allUsers.size === 0) {
            return [];
        }
        let userData = allUsers.get(username);
        return userData === undefined ? [] : userData;
    }

    const getAvatarByUsername = (username) => {
        let allProfileAvatars = new Map(JSON.parse(localStorage.getItem("profileAvatar")));
        if (allProfileAvatars === null || allProfileAvatars.size === 0) {
            return [];
        }
        let profileAvatar = allProfileAvatars.get(username);
        return profileAvatar === undefined ? [] : profileAvatar;
    }

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const relations = {
        SUBSCRIBED: "SUBSCRIBED",
        UNFOLLOWED: "UNSUBSCRIBED",
        OWNS: "OWNS"
    }

    const [dataLoaded, setDataLoaded] = useState(false);
    const [username, setUsername] = useState('');
    const [firstName, setFirstname] = useState('');
    const [lastName, setLastname] = useState('');
    const [postCount, setPostCount] = useState(0);
    const [bio, setBio] = useState('');
    const [followersCount, setFollowersCount] = useState(-1);
    const [followsCount, setFollowsCount] = useState(-1);

    const [relationToPage, setRelationToPage] = useState('')

    //subscription part
    const [followersModalActive, setFollowersModalActive] = useState(false);
    const [followsModalActive, setFollowsModalActive] = useState(false);
    const [followersLoading, setFollowersLoading] = useState(true);
    const [followsLoading, setFollowsLoading] = useState(true);
    const [profileAvatarIsLoading, setProfileAvatarIsLoading] = useState(true)

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

    const [profileAvatar, setProfileAvatar] = useState(
        JSON.parse(localStorage.getItem("profileAvatar")) === null ?
            [] : JSON.parse(localStorage.getItem("profileAvatar"))
    );


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
        setProfileAvatarIsLoading(true)
        dispatch(fetchAvatar(name.username))
            .then(() => {
                setProfileAvatar(
                    getAvatarByUsername(name.username)
                )
                setProfileAvatarIsLoading(false);
            })
    },[dispatch, name.username]);

    useEffect(() => {
        setDataLoaded(false);
        dispatch(loadUserData(name.username))
            .then(() => {
                setNextFollowersPage(0)
                setNextFollowsPage(0)
                setDataLoaded(true)
            });
    }, [dispatch, name]);

    useEffect(() => {
        if (dataLoaded) {
            const userData = getUserDataByUsername(name.username);
            setUsername(userData.username);
            setFirstname(userData.firstName);
            setLastname(userData.lastName);
            setPostCount(userData.postCount);
            setRelationToPage(userData.relation)
            setBio(userData.bio);
            setFollowersCount(userData.followersCount);
            setFollowsCount(userData.followsCount);
        }
    }, [dataLoaded]);

    const handleRelationButton = () => {
        switch (relationToPage) {
            case relations.OWNS:
                return navigate("edit");
            case relations.SUBSCRIBED:
                return axios.post(
                    "http://localhost:8080/api/v1/users/" + JSON.parse(localStorage.getItem("user")).username + "/unfollow/" + username, {
                        headers: {
                            Authorization: authHeader().Authorization,
                            "Content-Type": "application/json",
                        }
                    }).then(() => {
                        console.log("xxxx")
                        setFollowersCount(followersCount - 1)
                        setRelationToPage(relations.UNFOLLOWED)
                    })
            case relations.UNFOLLOWED:
                return axios.post(
                    "http://localhost:8080/api/v1/users/" + JSON.parse(localStorage.getItem("user")).username + "/follow/" + username, {
                        headers: {
                            Authorization: authHeader().Authorization,
                            "Content-Type": "application/json",
                        },
                    }
                ).then(() => {
                    console.log("xxxxXXX")
                    setFollowersCount(followersCount + 1)
                    setRelationToPage(relations.SUBSCRIBED)
                })
        }
    }

    const parseRelationButton = () => {
        switch (relationToPage) {
            case relations.OWNS:
                return "Edit profile";
            case relations.SUBSCRIBED:
                return "Unfollow";
            case relations.UNFOLLOWED:
                return "Follow";
        }
    }

    return (
        <>
            <div>
                <div className="container">
                    <div className="profile">
                        <div className="profile-image">
                                <img
                                    src={!profileAvatarIsLoading ?
                                        `data:image/jpg;base64,${profileAvatar.photoInputStream}` :
                                        'https://kartinki.pics/uploads/posts/2022-12/1669947655_3-kartinkin-net-p-pustoi-belii-fon-instagram-3.png'}
                                    alt='...'
                                />
                        </div>
                        <div className="profile-user-settings">
                            <h1 className="profile-user-name">{dataLoaded ? username : 'loading...'}</h1>
                            <button
                                onClick={() => handleRelationButton()}
                                className="profile-edit-btn"
                            >
                                {parseRelationButton()}
                            </button>
                        </div>
                        <div className="profile-stats">
                            <ul>
                                <li><span className="profile-stat-count">{dataLoaded ? postCount : ''}</span>{dataLoaded ? ' posts' : 'loading...'}</li>
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
