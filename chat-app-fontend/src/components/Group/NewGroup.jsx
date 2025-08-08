import { Avatar, Button, CircularProgress } from '@mui/material'
import React, { useState } from 'react'
import { BsArrowLeft, BsCheck2 } from 'react-icons/bs'
import { useDispatch, useSelector } from 'react-redux';
import { createGroupChat } from '../../redux/Chat/Action';

const NewGroup = ({ groupMember, setIsGroup }) => {
    const [isImageUploading, setIsImageUploading] = useState(false);

    const [groupImage, setGroupImage] = useState(null);

    const [groupName, setGroupName] = useState("");

    const dispatch = useDispatch();

    const token = localStorage.getItem("token");

    const { auth } = useSelector(store => store);

    const handleCreateGroup = () => {
        let userIds = [];
        for (let user of groupMember) {
            userIds.push(user.id);
        }
        const group = {
            userIds,
            chatName: groupName,
            chatImage: groupImage,
        }
        const data = {
            group,
            token,
        };
        dispatch(createGroupChat(data));
        setIsGroup(false);
    };

    const uploadToCloudnary = (pics) => {
        setIsImageUploading(true);
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
                console.log("imgurl", data)
                setGroupImage(data.url.toString());
                // setMessage("Profile image updated successfully");
                // setOpen(true);
                // console.log("imgUrl", data.url.toString());
                // const dataa = {
                //     id: auth.reqUser.id,
                //     token: localStorage.getItem("token"),
                //     data: { avatar: data.url.toString() },
                // };
                // dispatch(updateUser(dataa));
                setIsImageUploading(false);
            });
    };

    return (
        <div className="w-full h-full bg-slate-300">
            <div className="flex items-center space-x-10 bg-[#008069] text-white pt-16 px-10 pb-5">
                <BsArrowLeft className="cursor-pointer text-2xl font-bold" />
                <p className="text-xl font-semibold">New group</p>
            </div>
            <div className="flex flex-col justify-center items-center my-12 h-[50.6vh]">
                <label htmlFor="imgInput" className="relative">
                    {/* <img
                        className="rounded-full w-[15vw] h-[15vw] cursor-pointer"
                        src={groupImage || "https://cdn.pixabay.com/photo/2025/06/19/07/59/allgau-9668453_640.jpg"}
                        alt="" /> */}
                    <Avatar
                        sx={{ width: "15rem", height: "15rem" }}
                        alt=""
                        src={groupImage || "https://cdn.pixabay.com/photo/2025/06/19/07/59/allgau-9668453_640.jpg"} />
                    {isImageUploading && (
                        <CircularProgress className="absolute top-[5rem] left-[6rem]" />
                    )}
                </label>
                <input
                    type="file"
                    className="hidden"
                    id="imgInput"
                    onChange={(e) => uploadToCloudnary(e.target.files[0])}
                    value={""} />

            </div>
            <div className="w-full flex justify-between items-center py-2 px-5">
                <input
                    className="w-full outline-none border-b-2 border-green-700 px-2 bg-transparent"
                    type="text"
                    onChange={(e) => setGroupName(e.target.value)}
                    placeholder="Group subject"
                    value={groupName} />
            </div>

            {groupName && <div className="py-10 bg-slate-300 flex items-center justify-center">
                <Button onClick={handleCreateGroup}>
                    <div className="bg-[#0c977d] rounded-full p-4">
                        <BsCheck2 className="text-white font-bold text-2xl" />
                    </div>
                </Button>
            </div>}
        </div>
    )
}

export default NewGroup