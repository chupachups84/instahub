const  MyProfileHeader = () => {
    return (
        <>
            <header>
                <div className="container">
                    <div className="profile">
                        <div className="profile-image">
                            <img
                                src="https://sun1-98.userapi.com/impg/7D-NMJxJLdFOKgXYQGUUnbGYTkUvYXL8MGDsAA/_m4bXZZYoek.jpg?size=719x679&quality=96&sign=55c8fef48b6612c1f884d55888b731d2&type=album"
                                alt=""/>
                        </div>
                        <div className="profile-user-settings">
                            <h1 className="profile-user-name">chupachups84</h1>
                            <button className="btn profile-edit-btn">Edit Profile</button>
                        </div>
                        <div className="profile-stats">
                            <ul>
                                <li><span className="profile-stat-count">164</span> posts</li>
                                <li><span className="profile-stat-count">188</span> followers</li>
                                <li><span className="profile-stat-count">206</span> following</li>
                            </ul>
                        </div>
                        <div className="profile-bio">
                            <p><span className="profile-real-name">chupachups84</span>(GRUBIYAN)</p>
                        </div>
                    </div>
                </div>
            </header>
        </>
    )
}
export default MyProfileHeader;
