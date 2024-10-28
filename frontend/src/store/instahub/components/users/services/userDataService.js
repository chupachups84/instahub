import axios from "axios";
import authHeader from "../../authentication/services/authHeader";

class UserDataService {

    loadUserData(username) {
        return axios.all([
            axios.get("api/v1/users", {
                params: {
                    username: username
                },
                withCredentials: true,
                headers: authHeader().Authorization
            }),
            axios.get("api/v1/users/" + username + "/followers/count", {
                withCredentials: true,
                headers: authHeader().Authorization
            }),
            axios.get( "api/v1/users/" + username + "/follows/count", {
                withCredentials: true,
                headers: authHeader().Authorization
            }),
            axios.get("api/v1/users/" + username + "/relation", {
                params: {
                    username: JSON.parse(localStorage.getItem("user")).username
                },
                withCredentials: true,
                headers: {
                    Authorization: authHeader().Authorization
                },
            })
        ]).then(axios.spread((userDataResponse, followersCountResponse, followsCountResponse, relationResponse) => {

            if (userDataResponse.data && followersCountResponse.data && followsCountResponse.data) {

                let allUsers = JSON.parse(localStorage.getItem("userData")) === null ?
                    new Map() : new Map(JSON.parse(localStorage.getItem("userData")));

                userDataResponse.data.followersCount = followersCountResponse.data.count;
                userDataResponse.data.followsCount = followsCountResponse.data.count;
                userDataResponse.data.relation = relationResponse.data;
                allUsers.set(username, userDataResponse.data);
                localStorage.setItem("userData", JSON.stringify(Array.from(allUsers)));
            }
            return userDataResponse.data;
        })).catch(err => {
           console.log(err)
        });
    };
}

export default new UserDataService();