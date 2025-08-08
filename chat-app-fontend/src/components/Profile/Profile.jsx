import React, { useState } from 'react'
import { BsArrowLeft, BsCheck2, BsPencil } from 'react-icons/bs'
import { data, useNavigate } from 'react-router-dom'
import { updateUser } from '../../redux/Auth/Action';
import { useDispatch, useSelector } from 'react-redux';

const Profile = ({ handleCloseOpenProfile }) => {

    const [flag, setFlag] = useState(false);

    const navigate = useNavigate();

    const [username, serUsername] = useState(null);

    const [tempPicture, setTempPicture] = useState(null);

    const { auth } = useSelector(store => store);

    const dispatch = useDispatch();

    // const handleNavigate = () => {
    //     navigate(-1);
    // };

    const handleFlag = () => {
        setFlag(true);
    };

    const handleCheckClick = (e) => {
        setFlag(false);
        const data = {
            id: auth.reqUser?.id,
            token: localStorage.getItem("token"),
            data: { fullName: username },
        };

        dispatch(updateUser(data));

    };

    const handleChange = (e) => {
        serUsername(e.target.value);
    };

    const uploadToCloudnary = (pics) => {
        const data = new FormData();
        data.append("file", pics);
        data.append("upload_preset", "whats_app");
        data.append("cloud_name", "dcv6el7hw");
        fetch("https://api.cloudinary.com/v1_1/dcv6el7hw/image/upload", {
            method: "POST",
            body: data,
        })
            .then((res) => res.json())
            .then((data) => {
                setTempPicture(data.url.toString());
                // setMessage("Profile image updated successfully");
                // setOpen(true);
                console.log("imgUrl", data.url.toString());
                const dataa = {
                    id: auth.reqUser.id,
                    token: localStorage.getItem("token"),
                    data: { avatar: data.url.toString() },
                };
                dispatch(updateUser(dataa));
            });
    };

    const handleUpdateName = (e) => {
        const data = {
            id: auth.reqUser?.id,
            token: localStorage.getItem("token"),
            data: { fullName: username },
        };
        if (e.target.key === "Enter") {
            dispatch(updateUser(data));
        }
    };

    return (
        <div className="w-full h-full bg-white">
            <div className="flex items-center space-x-10 bg-[#008069] text-white pt-16 px-10 pb-5">
                <BsArrowLeft
                    className="cursor-pointer text-2xl font-bold"
                    onClick={handleCloseOpenProfile} />
                <p className="cursor-pointer font-semibold">Profile</p>
            </div>
            {/* update profile avatar */}
            <div className="flex flex-col justify-center items-center my-12">
                <label htmlFor="imgInput">
                    <img
                        className="rounded-full w-[15vw] h-[15vw] cursor-pointer"
                        src={auth.reqUser?.avatar || tempPicture || "https://cdn.pixabay.com/photo/2025/06/19/07/59/allgau-9668453_640.jpg"}
                        alt="" />
                </label>
                <input
                    onChange={(e) => uploadToCloudnary(e.target.files[0])}
                    type="file"
                    id="imgInput"
                    className="hidden" />
            </div>
            {/* name section */}
            <div className="bg-gray-200 px-3">
                <p className="py-3">Your name</p>
                {!flag && <div className="w-full flex justify-between items-center">
                    <p className="py-3">{auth.reqUser?.fullName || "username"}</p>
                    <BsPencil
                        onClick={handleFlag}
                        className="cursor-pointer" />
                </div>}

                {flag && <div className="w-full flex justify-between items-center py-2">
                    <input
                        onKeyPress={handleUpdateName}
                        onChange={handleChange}
                        className="w-[80%] outline-none border-b-2 border-blue-700 p-2"
                        type="text"
                        placeholder="Enter your name" />
                    <BsCheck2
                        onClick={handleCheckClick}
                        className="cursor-pointer text-2xl" />
                </div>}

            </div>
            <div className="px-3 my-5">
                <p className="py-10 text-center">This is not your username, this name will be visible to your whatsapp contacts</p>
            </div>
        </div>
    )
}

export default Profile