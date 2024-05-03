import {} from "./ProfileMain.css"
import {} from "../../pages/my-profile/ProfilePage.css"
import {useDispatch} from "react-redux";
import {Dispatch} from "redux";
import {fetchPhotos} from "../../store/instahub/components/profile/actions/profileActionsCreator";
import {useState} from "react";


const ProfileMain = (username) => {

    const dispatch: Dispatch = useDispatch();

    const [profilePhotos, setProfilePhotos] = useState([]);

    const [page, setPage] = useState(0);

    let size = 3;

    const fetchNextPage = () => {
        setPage(page + 1);
        dispatch(fetchPhotos(page, size, username.username));
        setProfilePhotos(
            JSON.parse(localStorage.getItem("profilePhotos")) === null ?
            [] : JSON.parse(localStorage.getItem("profilePhotos"))
        )
        console.log("page: " + page)
        console.log("prof photos: " + profilePhotos.length)
    }


    document.addEventListener(
        'DOMContentLoaded', function() {
            if (JSON.parse(localStorage.getItem("profilePhotos")) === null) {
                dispatch(fetchPhotos(0, size, username.username));
            }
            // else {
            //     setProfilePhotos(
            //         JSON.parse(localStorage.getItem("profilePhotos"))
            //     );
            // }
            setProfilePhotos(
                JSON.parse(localStorage.getItem("profilePhotos")) === null ?
                    [] : JSON.parse(localStorage.getItem("profilePhotos"))
            );
            fetchNextPage();
            console.log("first render: " + profilePhotos.length)
        }
    )

    return (
        <div>
            <div className="container">
                <div className="gallery">
                    {profilePhotos.map((photoBase64) => (
                        <div
                            className="gallery-item"
                            tabIndex="0"
                        >
                            <img src={`data:image/jpg;base64,${photoBase64}`}
                                 className="gallery-image" alt=""/>
                            <div className="gallery-item-info">
                                <ul>
                                    <li className="gallery-item-likes"><span className="visually-hidden">Likes:</span><i
                                        className="fas fa-heart" aria-hidden="true"></i> 56
                                    </li>
                                    <li className="gallery-item-comments"><span
                                        className="visually-hidden">Comments:</span><i className="fas fa-comment"
                                                                                       aria-hidden="true"></i> 2
                                    </li>
                                </ul>
                            </div>
                        </div>
                    ))}
                </div>
                <button onClick={() => fetchNextPage()}>Load more</button>
            </div>
        </div>
    )
}
export default ProfileMain;