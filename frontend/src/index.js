import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import SignUpPage from "./pages/sign-up/SignUpPage";
import reportWebVitals from './reportWebVitals';
import {
    createBrowserRouter,
    RouterProvider,
} from "react-router-dom";
import HomePage from "./pages/home/HomePage";
import SignInPage from "./pages/sign-in/SignInPage";
import UserProfile from "./components/UserProfile";

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
        path: "/profile",
        element: <UserProfile/>
    }
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
          <RouterProvider router={router} />
  </React.StrictMode>
);

reportWebVitals();
