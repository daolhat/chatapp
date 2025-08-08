import { Alert, Button, Snackbar } from '@mui/material';
import { green } from '@mui/material/colors';
import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { currentUser, login } from '../../redux/Auth/Action';
import { store } from '../../redux/store';

const Signin = () => {
    const [openSnackBar, setOpenSnackBar] = useState(false);

    const navigate = useNavigate();

    const [inputData, setInputData] = useState({ email: "", password: "" });

    const dispatch = useDispatch();

    const { auth } = useSelector(store => store);

    const token = localStorage.getItem("token");

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log("handle submit");
        setOpenSnackBar(true);
        dispatch(login(inputData));
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setInputData((values) => ({ ...values, [name]: value }))
    };

    const handleSnackbarClose = () => {
        setOpenSnackBar(false);
    };


    useEffect(() => {
        if (token) dispatch(currentUser(token))
    }, [token]);

    useEffect(() => {
        if (auth.reqUser?.fullName) {
            navigate("/")
        }
    }, [auth.reqUser]);

    return (
        <div className="">
            <div className="">
                <div className="flex justify-center min-h-screen items-center">
                    <div className="w-[25%] p-10 shadow-md bg-white">
                        <form action="" onSubmit={handleSubmit} className="space-y-5">
                            <div>
                                <p className="mb-2">Email</p>
                                <input type="text" className="py-2 px-3 outline outline-green-600 w-full rounded-md border"
                                    placeholder="Enter your email"
                                    value={inputData.email}
                                    onChange={handleChange}
                                    name="email" />
                            </div>
                            <div>
                                <p className="mb-2">Password</p>
                                <input type="text" className="py-2 px-3 outline outline-green-600 w-full rounded-md border"
                                    placeholder="Enter your password"
                                    value={inputData.password}
                                    onChange={handleChange}
                                    name="password" />
                            </div>
                            <div>
                                <Button type="submit" sx={{ bgcolor: green[700], padding: ".5rem 0rem" }} className="w-full bg-green-700" variant="contained" >Sign in</Button>
                            </div>
                        </form>
                        <div className="flex space-x-3 items-center mt-5">
                            <p className="">
                                Create new account!
                            </p>
                            <Button className="" variant="text"
                                onClick={() => navigate("/signup")} >Sign up</Button>
                        </div>
                    </div>
                </div>
                <Snackbar open={openSnackBar} autoHideDuration={6000} onClose={handleSnackbarClose}>
                    <Alert
                        onClose={handleSnackbarClose}
                        severity="success"
                        variant="filled"
                        sx={{ width: '100%' }}
                    >
                        This is a success Alert inside a Snackbar!
                    </Alert>
                </Snackbar>
            </div>
        </div>
    )
}

export default Signin