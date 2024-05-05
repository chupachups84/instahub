import {
    LOAD_USER_DATA_FAILED,
    LOAD_USER_DATA_SUCCEEDED
} from "../actions/userDataActionTypes";

const userData = JSON.parse(localStorage.getItem("userData"));

const initialState = userData
    ? {
        isLoading: true,
        userData
    }
    : {
        isLoading: false,
        userData: null
    };

export default function (state = initialState, action) {

    const { type, payload } = action;

    switch (type) {
        case LOAD_USER_DATA_SUCCEEDED:
            return {
                ...state,
                isLoading: true,
                userData: payload.userData,
            };
        case LOAD_USER_DATA_FAILED:
            return {
                ...state,
                isLoading: false,
                userData: null,
            };
        default:
            return state;
    }
}