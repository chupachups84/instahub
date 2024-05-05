import authHeader from "../../authentication/services/authHeader";
import axios from "axios";

class ProfileService {

    fetchPhotos(page, size, username) {
        return axios.get("/api/v1/photos/load/icons", {
            withCredentials: true,
            headers: authHeader().Authorization,
            params: {
                page: page,
                size: size,
                username: username
            }
        })
            .then(response => {
                if (response.data) {
                    let allPhotos = JSON.parse(localStorage.getItem("profilePhotos")) === null ?
                        new Map() : new Map(JSON.parse(localStorage.getItem("profilePhotos")));
                    let storedPhotos = allPhotos.get(username);
                    if (storedPhotos === undefined || page === 0) {
                        allPhotos.set(username, response.data);
                    } else {
                        for (const v in response.data) {
                            storedPhotos.push(response.data[v]);
                        }
                    }
                    localStorage.setItem("profilePhotos", JSON.stringify(Array.from(allPhotos)));
                }
                return response.data;
            })
            .catch(err => {
                console.log(err)
            })
    }

    fetchAvatar(username) {

        return axios.get("/api/v1/photos/load/avatar", {
            withCredentials: true,
            headers: authHeader().Authorization,
            params: {
                username: username,
            }
        })
            .then(response => {
                if (response.data) {
                    let allAvatars = JSON.parse(localStorage.getItem("profileAvatar")) === null ?
                        new Map() : new Map(JSON.parse(localStorage.getItem("profileAvatar")));

                    allAvatars.set(username, response.data);

                    localStorage.setItem("profileAvatar", JSON.stringify(Array.from(allAvatars)));
                    return response.data;
                }
            })
            .catch(err => {
                console.log(err)
            })
    }
}

export default new ProfileService();