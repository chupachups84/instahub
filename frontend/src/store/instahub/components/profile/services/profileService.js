import axios from "axios";
import authHeader from "../../authentication/services/authHeader";

const API_URL = "http://localhost:8080/api/v1"


class ProfileService {

    fetchPhotos(page, size, username) {
        return axios.get(API_URL + "/photos/load/icons", {
            headers: {
                Authorization: authHeader().Authorization,
                "Content-Type": "application/json",
            },
            params: {
                page: page,
                size: size,
                username: username
            }
        })
            .then(response => {
                console.log("entry")
                if (response.data) {
                    let storedPhotos = JSON.parse(localStorage.getItem("profilePhotos"));
                    if (storedPhotos === null) {
                        storedPhotos = response.data
                    } else {
                        if (page !== 0) {
                            for (const v in response.data) {
                                storedPhotos.push(response.data[v]);
                            }
                        }
                    }
                    console.log(storedPhotos)
                    localStorage.setItem("profilePhotos", JSON.stringify(storedPhotos));
                    console.log(JSON.parse(localStorage.getItem("profilePhotos")))
                }
                return response.data;
            })
            .catch(err => {
                console.log(err)
            })
    }
}

export default new ProfileService();