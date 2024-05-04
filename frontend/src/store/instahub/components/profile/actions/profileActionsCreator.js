import {
    FETCH_PHOTOS_FAILED,
    FETCH_PHOTOS_SUCCEEDED,
} from "./profileActionTypes";

import profileService from "../services/profileService";

export const fetchPhotos = (page, size, username) => (dispatch) => {

    return profileService.fetchPhotos(page, size, username)
        .then(
            (data) => {
                dispatch({
                    type: FETCH_PHOTOS_SUCCEEDED,
                    payload: { profilePhotos:data },
                });
                console.log(Promise.resolve());
                return Promise.resolve();
            },
            (error) => {

                dispatch({
                    type: FETCH_PHOTOS_FAILED,
                });

                return Promise.reject();
            }
        );
};
