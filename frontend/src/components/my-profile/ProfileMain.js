import "./ProfileMain.css"
import "../../pages/my-profile/ProfilePage.css"
import {useDispatch} from "react-redux";
import {Dispatch} from "redux";
import {fetchPhotos} from "../../store/instahub/components/profile/actions/profileActionsCreator";
import {useEffect, useState} from "react";

const ProfileMain = (username) => {

    const dispatch: Dispatch = useDispatch();


    const [dataLoaded, setDataLoaded] = useState(false);

    const [profilePhotos, setProfilePhotos] = useState(
        JSON.parse(localStorage.getItem("profilePhotos")) === null ?
            [] : JSON.parse(localStorage.getItem("profilePhotos"))
    );

    let size = 3;

    const [nextPage, setNextPage] = useState(
        JSON.parse(localStorage.getItem("profilePhotos")) === null ?
            1 : Math.floor((JSON.parse((localStorage.getItem("profilePhotos"))).length - 1) / size) + 1
    );


    const fetchNextPage = (page) => {
        console.log("page = " + page)
        dispatch(fetchPhotos(page, size, username.username))
            .then(
                () => {
                    setProfilePhotos(
                        JSON.parse(localStorage.getItem("profilePhotos")) === null ?
                            [] : JSON.parse(localStorage.getItem("profilePhotos"))
                    )
                }
            );
        setNextPage(page + 1);
    }

    useEffect(() => {
        if (localStorage.getItem('profilePhotos') === null) {
            setDataLoaded(false);
            dispatch(fetchPhotos(0, size, username.username))
                .then(() => {
                    setDataLoaded(true);
                    setNextPage(1)
                });
        } else {
            setDataLoaded(true)
        }
    }, [dispatch, username.username]);

    useEffect(() => {
        if (dataLoaded) {
            const storeProfilePhotos = JSON.parse(localStorage.getItem('profilePhotos'));
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