import "./ProfileMain.css"
import "../../pages/my-profile/ProfilePage.css"
import {useDispatch} from "react-redux";
import {Dispatch} from "redux";
import {fetchPhotos} from "../../store/instahub/components/profile/actions/profileActionsCreator";
import {useEffect, useState} from "react";

const ProfileMain = (name) => {

    const getPhotosByUsername = (username) => {
        let allPhotos = new Map(JSON.parse(localStorage.getItem("profilePhotos")));
        if (allPhotos === null || allPhotos.size === 0) {
            return [];
        }
        let storedPhotos = allPhotos.get(username);
        return storedPhotos === undefined ? [] : storedPhotos;
    }

    const dispatch: Dispatch = useDispatch();

    const username = name.username;

    const [dataLoaded, setDataLoaded] = useState(false);

    const [profilePhotos, setProfilePhotos] = useState(
        getPhotosByUsername(username)
    );

    let size = 3;

    const [nextPage, setNextPage] = useState(
        profilePhotos.length === 0 ?
            1 : Math.floor(profilePhotos.length / size) + 1
    );


    const fetchNextPage = (page) => {
        console.log("page = " + page)
        dispatch(fetchPhotos(page, size, username))
            .then(
                () => {
                    setProfilePhotos(
                        getPhotosByUsername(username)
                    )
                }
            );
        setNextPage(page + 1);
    }

    useEffect(() => {
        if (getPhotosByUsername(username).length === 0) {
            setDataLoaded(false);
            console.log(username)
            dispatch(fetchPhotos(0, size, username))
                .then(() => {
                    setDataLoaded(true);
                    setNextPage(1)
                });
        } else {
            setDataLoaded(true)
        }
    }, [dispatch, username]);

    useEffect(() => {
        if (dataLoaded) {
            const storeProfilePhotos = getPhotosByUsername(username);
            setProfilePhotos(storeProfilePhotos == null ? [] : storeProfilePhotos);
        }
    }, [dataLoaded]);

    return (
        <div>
            <div className="container">
                <div className="gallery">
                    {!dataLoaded ? '' : profilePhotos.map((photoBase64, index) => (
                        <div
                            onClick={() =>
                                console.log(index) //insert here axios FeedPhotoDto call (creationOffset equals index)
                            }
                            className="gallery-item"
                            tabIndex="0"
                        >
                            <img src={dataLoaded ? `data:image/jpg;base64,${photoBase64}` : ''}
                                 className="gallery-image"
                                 alt="image"
                            />
                            <div className="gallery-item-info">

                            {/*   FOLLOWING CODE IS BROKEN BUT MIGHT BE USED IN FUTURE    */}

                            {/*    <ul>*/}
                            {/*        <li className="gallery-item-likes"><span className="visually-hidden">Likes:</span><i*/}
                            {/*            className="fas fa-heart" aria-hidden="true"></i> 56*/}
                            {/*        </li>*/}
                            {/*        <li className="gallery-item-comments"><span*/}
                            {/*            className="visually-hidden">Comments:</span><i className="fas fa-comment"*/}
                            {/*                                                           aria-hidden="true"></i> 2*/}
                            {/*        </li>*/}
                            {/*    </ul>*/}

                            </div>
                        </div>
                    ))}
                </div>
                <button onClick={() => fetchNextPage(nextPage)}>Load more</button>
            </div>
        </div>
    )
}
export default ProfileMain;