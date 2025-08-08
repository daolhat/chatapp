import { Alert, Button, Snackbar } from '@mui/material';
import { green } from '@mui/material/colors';
import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { currentUser, register } from '../../redux/Auth/Action';
import { store } from '../../redux/store';

const Signup = () => {

    const [openSnackBar, setOpenSnackBar] = useState(false);

    const navigate = useNavigate();

    const [inputData, setInputData] = useState({ fullName: "", email: "", password: "" });

    const dispatch = useDispatch();

    const {auth} = useSelector(store=>store);

    const token = localStorage.getItem("token");

    console.log("current user", auth.reqUser);

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log("handle submit", inputData);
        dispatch(register(inputData))
        setOpenSnackBar(true);
    };

    const handleChange = (e) => {
        const {name, value } = e.target;
        setInputData((values) => ({...values, [name]:value}))
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
                <div className="flex flex-col justify-center min-h-screen items-center">
                    <div className="w-[25%] bg-white p-10 shadow-md">
                        <form action="" onSubmit={handleSubmit} className="space-y-5">
                            <div>
                                <p className="mb-2">
                                    Username
                                </p>
                                <input type="text" className="py-2 px-3 outline outline-green-600 w-full rounded-md border-1"
                                    placeholder="Enter your username"
                                    value={inputData.fullName}
                                    name="fullName"
                                    onChange={(e) => handleChange(e)}
                                />
                            </div>
                            <div>
                                <p className="mb-2">
                                    Email
                                </p>
                                <input type="text" className="py-2 px-3 outline outline-green-600 w-full rounded-md border-1"
                                    placeholder="Enter your email"
                                    value={inputData.email}
                                    name="email"
                                    onChange={(e) => handleChange(e)}
                                />
                            </div>
                            <div>
                                <p className="mb-2">
                                    Password
                                </p>
                                <input type="text" className="py-2 px-3 outline outline-green-600 w-full rounded-md border-1"
                                    placeholder="Enter your password"
                                    value={inputData.password}
                                    name="password"
                                    onChange={(e) => handleChange(e)}
                                />
                            </div>
                        
                            <div className="">
                                <Button type="submit" sx={{ bgcolor: green[700], padding: ".5rem 0rem" }} className="w-full bg-green-700" variant="contained">
                                    Sign up
                                </Button>
                            </div>

                        </form>
                        <div className="flex space-x-3 items-center mt-5">
                            <p className="">Already have account?</p>
                            <Button className="" variant="text"
                                onClick={() => navigate("/signin")} >Sign in</Button>
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

export default Signup