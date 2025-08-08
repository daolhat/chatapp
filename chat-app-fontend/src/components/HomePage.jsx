import React, { useEffect, useState } from 'react';
import { AiOutlineSearch } from 'react-icons/ai';
import { BiCommentDetail } from 'react-icons/bi';
import { BsEmojiSmile, BsFilter, BsMicFill, BsThreeDotsVertical } from 'react-icons/bs';
import { TbCircleDashed } from 'react-icons/tb';
import ChatCard from './ChatCard/ChatCard';
import imagelight from '../assects/imagelight.png';
import MessageCard from './MessageCard/MessageCard';
import { ImAttachment } from 'react-icons/im';
import { MdSend } from 'react-icons/md';
import './HomePage.css';
import { useNavigate } from 'react-router-dom';
import Profile from './Profile/Profile';
import { Button, Menu, MenuItem } from '@mui/material';
import CreateGroup from './Group/CreateGroup';
import { useDispatch, useSelector } from 'react-redux';
import { currentUser, logoutAction, searchUser } from '../redux/Auth/Action';
import { store } from '../redux/store';
import { createChat, getUsersChat } from '../redux/Chat/Action';
import { createMessage, getAllMessages } from '../redux/Message/Action';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

const HomePage = () => {

    const [querys, setQuerys] = useState(null);

    const [currentChat, setCurretChat] = useState(null);

    const [content, setContent] = useState("");

    const [isProfile, setIsProfile] = useState(false);

    const navigate = useNavigate();

    const [isGroup, setIsGroup] = useState(false);

    const dispatch = useDispatch();

    const { auth, chat, message } = useSelector(store => store);

    const token = localStorage.getItem("token");

    const [anchorEl, setAnchorEl] = React.useState(null);

    const open = Boolean(anchorEl);

    const [stompClient, setStompClient] = useState();

    const [isConnect, setIsConnect] = useState(false);

    const [messages, setMessages] = useState([]);

    const connect = () => {
        const sock = new SockJS("http://localhost:3000/ws");
        const temp = Stomp.over(sock);
        setStompClient(temp);
        const headers = {
            Authorization: `Bearer ${token}`,
            "X-XSRF-TOKEN": getCookies("XSRT-TOKEN"),
        }
        temp.connect(headers, onConnerct, onError);
    };

    function getCookies(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) {
            return parts.pop().split(";").shift();
        }
    };

    const onError = (error) => {
        console.log("no error", error);
    }

    const onConnerct = () => {
        setIsConnect(true);
    };

    useEffect(() => {
        if (message.newMessage && stompClient) {
            setMessages([...messages, message.newMessage]);
            stompClient?.send("/app/message", {}, JSON.stringify(message.newMessage));
        }
    }, [message.newMessage]);

    const onMessageReceive = (payload) => {
        console.log("receive message", JSON.parse(payload.body));
        const receiveMessage = JSON.parse(payload.body);
        setMessages([...messages, receiveMessage]);
    }

    useEffect(() => {
        if (isConnect && stompClient && auth.reqUser && currentChat) {
            const subcription = stompClient.subscribe("/group/" + currentChat.id.toString, onMessageReceive);

            return () => {
                subcription.unsubscribe();
            }
        }
    })

    useEffect(() => {
        connect()
    }, [])

    useEffect(() => {
        setMessages(message.messages);
    }, [message.messages]);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleClickOnChatCard = (userId) => {
        // setCurretChat(true);
        dispatch(createChat({ token, data: { userId } }));
        console.log("token send", token);
        setQuerys("");
    };

    const handleSearch = (keyword) => {
        dispatch(searchUser({ keyword, token }));
    };

    const handleCreateNewMessage = () => {
        dispatch(createMessage({
            token,
            data: { chatId: currentChat.id, content: content }
        }))
    };

    useEffect(() => {
        dispatch(getUsersChat(token));
    }, [chat.createdChat, chat.createdGroup])

    useEffect(() => {
        if (currentChat?.id)
            dispatch(getAllMessages({ chatId: currentChat.id, token }));
    }, [currentChat, message.newMessage]);

    const handleNavigate = () => {
        // navigate("/profile")
        setIsProfile(true);
    };

    const handleCloseOpenProfile = () => {
        setIsProfile(false);
    };

    const handleCreateGroup = () => {
        setIsGroup(true);
    };

    useEffect(() => {
        dispatch(currentUser(token));
    }, [token]);

    const handleLogout = () => {
        dispatch(logoutAction());
        navigate("/signin")
    };

    useEffect(() => {
        if (!auth.reqUser) {
            navigate("/signin");
        }
    }, [auth.reqUser]);

    const handleCurrentChat = (item) => {
        setCurretChat(item);
    };

    console.log("current chat");

    return (
        <div className="relative flex justify-center items-center">
            <div className="py-14 bg-[#00a884] w-full"></div>

            {/* left-[2vw] */}
            <div className="flex bg-[#f0f3f5] h-[94vh] absolute top-6 w-[96vw]">
                <div className="left w-[30%] bg-[#e8e9ec] h-full">

                    {/* profile */}
                    {isProfile && <div className="w-full h-full"> <Profile handleCloseOpenProfile={handleCloseOpenProfile} /> </div>}

                    {/* group */}
                    {isGroup && <CreateGroup setIsGroup={setIsGroup} />}

                    {/* home */}
                    {!isProfile && !isGroup && <div className="w-full">

                        <div className="flex justify-between items-center p-3">
                            <div onClick={handleNavigate} className="flex items-center space-x-3">
                                <img
                                    className="rounded-full w-10 h-10 cursor-pointer"
                                    src={auth.reqUser?.avatar ||
                                        "https://cdn.pixabay.com/photo/2025/06/19/07/59/allgau-9668453_640.jpg"
                                    }
                                    alt="" />
                                <p>{auth.reqUser?.fullName}</p>
                            </div>
                            <div className="space-x-3 text-2xl flex">
                                <TbCircleDashed className="cursor-pointer" onClick={() => navigate("/status")} />
                                <BiCommentDetail />
                                <div>
                                    <BsThreeDotsVertical className="cursor-pointer"
                                        id="basic-button"
                                        aria-controls={open ? 'basic-menu' : undefined}
                                        aria-haspopup="true"
                                        aria-expanded={open ? 'true' : undefined}
                                        onClick={handleClick} />
                                    <Menu
                                        id="basic-menu"
                                        anchorEl={anchorEl}
                                        open={open}
                                        onClose={handleClose}
                                        slotProps={{
                                            list: {
                                                'aria-labelledby': 'basic-button',
                                            },
                                        }} >
                                        {/* <MenuItem onClick={handleClose}>Profile</MenuItem> */}
                                        <MenuItem onClick={handleCreateGroup}>Create group</MenuItem>
                                        <MenuItem onClick={handleLogout}>Logout</MenuItem>
                                    </Menu>
                                </div>

                            </div>
                        </div>
                        <div className="relative flex justify-center items-center bg-white py-4 px-3">
                            <input className="border-none outline-none bg-slate-200 rounded-md w-[93%] pl-9 py-2"
                                type="text"
                                placeholder="Search or start new chat"
                                onChange={(e) => {
                                    setQuerys(e.target.value)
                                    handleSearch(e.target.value)
                                }}
                                value={querys} />
                            <AiOutlineSearch className="left-6 top-7 absolute" />
                            <div>
                                <BsFilter className="ml-4 text-3xl" />
                            </div>
                        </div>
                        {/* all user */}
                        <div className="bg-white overflow-y-scroll h-[79.2vh] px-3">
                            {querys && auth.searchUser?.map((item) => (
                                <div className="" onClick={() => handleClickOnChatCard(item.id)}>
                                    {" "}
                                    <hr />
                                    <ChatCard name={item.fullName}
                                        userImg={
                                            item.avatar ||
                                            "https://cdn.pixabay.com/photo/2025/06/19/07/59/allgau-9668453_640.jpg"
                                        } />
                                </div>
                            ))}
                            {chat.chats?.length > 0 && !querys && chat.chats?.map((item) => (
                                <div className="" onClick={() => handleCurrentChat(item)}>

                                    <hr />
                                    {item.isGroup ?
                                        (<ChatCard
                                            name={item.chatName}
                                            userImg={
                                                item.chatImage ||
                                                "https://cdn.pixabay.com/photo/2025/06/19/07/59/allgau-9668453_640.jpg"
                                            } />
                                        ) : (<ChatCard
                                            isChat={true}
                                            name={
                                                auth.reqUser?.id !== item.users[0]?.id
                                                    ? item.users[0].fullName
                                                    : item.users[1].fullName
                                            }
                                            userImg={
                                                auth.reqUser.id !== item.users[0].id
                                                    ? item.users[0].avatar ||
                                                    "https://cdn.pixabay.com/photo/2025/06/19/07/59/allgau-9668453_640.jpg"
                                                    : item.users[1].avatar ||
                                                    "https://cdn.pixabay.com/photo/2025/06/19/07/59/allgau-9668453_640.jpg"
                                            } />)
                                    }
                                </div>
                            ))}
                        </div>
                    </div>}
                </div>

                {/* default chat app page */}

                {!currentChat && <div className="w-[70%] flex flex-col justify-center items-center h-full">
                    <div className="max-w-[70%] text-center">
                        <img src={imagelight} alt="" />
                        <h1 className="text-4xl text-gray-600">WhatsApp Web</h1>
                        <p className="my-9">send and receive message without keeping your phone online. Use WhatsApp on Up to 4 Linked devices
                            and 1 phone at the same time.
                        </p>
                    </div>
                </div>}

                {/* message part */}
                {currentChat && (<div className="w-[70%] relative">

                    <div className="header absolute top-0 w-full bg-[#f0f2f5]">
                        <div className="flex justify-between">
                            <div className="py-3 space-x-4 flex items-center px-3">
                                <img className="w-10 h-10 rounded-full"
                                    src={
                                        currentChat.isGroup ? currentChat.chatImage :
                                            (auth.reqUser.id !== currentChat.users[0].id
                                                ? currentChat.users[0].avatar ||
                                                "https://cdn.pixabay.com/photo/2025/06/19/07/59/allgau-9668453_640.jpg"
                                                : currentChat.users[1].avatar ||
                                                "https://cdn.pixabay.com/photo/2025/06/19/07/59/allgau-9668453_640.jpg")
                                    } alt=""
                                />
                                <p>{currentChat.isGroup ? currentChat.chatName :
                                    (auth.reqUser?.id === currentChat.users[0].id ? currentChat.users[1].fullName : currentChat.users[0].fullName)}</p>
                            </div>
                            <div className="py-3 flex space-x-4 items-center px-3">
                                <AiOutlineSearch />
                                <BsThreeDotsVertical />
                            </div>
                        </div>
                    </div>

                    {/* message section */}
                    <div className="px-10 h-[85vh] overflow-y-scroll bg-blue-200">
                        <div className="space-y-1 flex flex-col justify-center mt-20 py-2">
                            {/* {message.messages?.length > 0 && message.messages?.map((item, i) => ( */}
                            {messages?.length > 0 && messages?.map((item, i) => (
                                <MessageCard
                                    isReqUserMessage={item.user.id !== auth.reqUser.id}
                                    content={item.content} />
                            ))}
                        </div>
                    </div>

                    {/* footer part */}
                    <div className="footer bg-[#f0f2f5] absolute bottom-0 w-full py-3 text-xl">
                        <div className="flex justify-between items-center px-5 relative">
                            {/* <div className="flex justify-between items-center space-x-4"> */}
                            <BsEmojiSmile className="cursor-pointer" />
                            <ImAttachment />
                            {/* </div> */}
                            <input className="py-2 outline-none border-none bg-white pl-4 rounded-md w-[85%]"
                                type="text"
                                onChange={(e) => setContent(e.target.value)}
                                placeholder="Type message"
                                value={content}
                                onKeyUp={(e) => {
                                    if (e.key === "Enter") {
                                        handleCreateNewMessage();
                                        setContent("");
                                    }
                                }} />
                            <BsMicFill />
                            <button>
                                <MdSend />
                            </button>
                        </div>
                    </div>
                </div>)}
            </div>
        </div>
    )
}

export default HomePage