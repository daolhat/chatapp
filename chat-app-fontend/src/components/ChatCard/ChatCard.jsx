import React from 'react'

const ChatCard = ({ userImg, name }) => {
    return (
        <div className="flex justify-center items-center py-2 group cursor-pointer">
            <div className="w-[20%]">
                <img className="h-14 w-14 rounded-full"
                    src={userImg ||
                        "https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png"} alt="" />
            </div>
            <div className="pl-5 w-[80%]">
                <div className="flex justify-between items-center">
                    <p className="text-lg">{name}</p>
                    <p className="text-sm">timestamp</p>
                </div>
                <div className="flex justify-between items-center">
                    <p>message...</p>
                    <div className="flex space-x-2 items-center">
                        <p className="text-xs py-1 px-2 text-white bg-green-500 rounded-full">5</p>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ChatCard