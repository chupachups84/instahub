import userDataService from "../services/userDataService";
import {
    LOAD_USER_DATA_FAILED,
    LOAD_USER_DATA_SUCCEEDED,
    LOAD_USER_RELATION_FAILED,
    LOAD_USER_RELATION_SUCCEEDED
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

export const loadUserRelation = (username) => (dispatch) => {

    return userDataService.loadUserRelation(username)
        .then(
            (data) => {
                dispatch({
                    type: LOAD_USER_RELATION_SUCCEEDED,
                    payload: { userRelation:data }
                })

                return Promise.resolve();
            },
            (error) => {

                dispatch({
                    type: LOAD_USER_RELATION_FAILED,
                });

                return Promise.reject();
            }
        )
}
