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
                    username: username
                },
                headers: authHeader().Authorization,
                withCredentials: true
            })
        ]).then(axios.spread((userDataResponse, followersCountResponse, followsCountResponse, relationResponse) => {
            if (userDataResponse.data && followersCountResponse.data && followsCountResponse.data) {
                userDataResponse.data.followersCount = followersCountResponse.data.count;
                userDataResponse.data.followsCount = followsCountResponse.data.count;
                userDataResponse.data.relation = relationResponse.data;
                localStorage.setItem("userData", JSON.stringify(userDataResponse.data));
            }
            return userDataResponse.data;
        })).catch(err => {
           console.log(err)
        });
    };
}

export default new UserDataService();