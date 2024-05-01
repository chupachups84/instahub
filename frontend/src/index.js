import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import SignUpPage from "./pages/sign-up/SignUpPage";
import reportWebVitals from './reportWebVitals';
import {createBrowserRouter, RouterProvider,} from "react-router-dom";
import HomePage from "./pages/home/HomePage";
import SignInPage from "./pages/sign-in/SignInPage";
import MyProfilePage from "./pages/my-profile/MyProfilePage";
import {Provider} from "react-redux";
import store from "./store/instahub/components/authentication/store";

const router = createBrowserRouter([
    {
        path: "/",
        element: <HomePage/>
    },
    {
        path: "/sign-up",
        element: <SignUpPage/>
    },
    {
        path: "/sign-in",
        element: <SignInPage/>
    },
    {
        path: "/:id",
        element: <MyProfilePage/>
    }
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <Provider store={store}>
        <React.StrictMode>
            <RouterProvider router={router} />
        </React.StrictMode>
    </Provider>

);

reportWebVitals();
