import userDataService from "../services/userDataService";
import {
    LOAD_USER_DATA_FAILED,
    LOAD_USER_DATA_SUCCEEDED
} from "./userDataActionTypes";

export const loadUserData = (username) => (dispatch) => {

    return userDataService.loadUserData(username)
        .then(
            (data) => {
                dispatch({
                    type: LOAD_USER_DATA_SUCCEEDED,
                    payload: { userData:data }
                })

                return Promise.resolve();
            },
            (error) => {

                dispatch({
                    type: LOAD_USER_DATA_FAILED,
                });

                return Promise.reject();
            }
        )

}