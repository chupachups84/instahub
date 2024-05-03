import ProfileHeader from "../../components/my-profile/ProfileHeader";
import ProfileMain from "../../components/my-profile/ProfileMain";
import {useParams} from "react-router-dom";

const ProfilePage = () => {
    const { username } = useParams();
    return (
        <>
            <ProfileHeader username={username}/>
            <ProfileMain username={username}/>
        </>
    )
}

export default ProfilePage;