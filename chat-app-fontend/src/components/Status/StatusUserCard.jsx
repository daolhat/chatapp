import React from 'react'
import { useNavigate } from 'react-router-dom'

const StatusUserCard = () => {

    const navigate = useNavigate();
    const handleNavigate = () => {
        navigate(`/status/{userId}`)
    }

    return (
        <div onClick={handleNavigate} className="flex items-center p-3 cursor-pointer">
            <div>
                <img className="h-7 w-7 lg:w-10 lg:h-10 rounded-full" src="https://cdn.pixabay.com/photo/2025/06/19/07/59/allgau-9668453_640.jpg" alt="" />
            </div>
            <div className="ml-2 text-white">
                <p>username</p>
            </div>
        </div>
    )
}

export default StatusUserCard