import ProfileHeader from "../../components/my-profile/ProfileHeader";
import ProfileMain from "../../components/my-profile/ProfileMain";

const ProfilePage = (username) => {
    return (
        <>
            <ProfileHeader username={username.username}/>
            <ProfileMain username={username.username}/>
        </>
    )
}

export default ProfilePage;