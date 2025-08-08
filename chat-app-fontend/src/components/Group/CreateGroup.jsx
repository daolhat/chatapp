import React, { useState } from 'react'
import { BsArrowLeft, BsArrowRight } from 'react-icons/bs'
import SelectedMember from './SelectedMember';
import ChatCard from '../ChatCard/ChatCard';
import NewGroup from './NewGroup';
import { searchUser } from '../../redux/Auth/Action';
import { useDispatch, useSelector } from 'react-redux';

const CreateGroup = ({ setIsGroup }) => {

    const [newGroup, setNewGroup] = useState(false);

    const [groupMember, setGroupMember] = useState(new Set());

    const [query, setQuery] = useState("");

    const dispatch = useDispatch();

    const token = localStorage.getItem("token");

    const { auth } = useSelector(store => store);

    const handleRemoveMember = (item) => {
        groupMember.delete(item);
        setGroupMember(groupMember);
    };

    const handleSearch = () => {
        dispatch(searchUser({ keyword: query, token }));
    };


    return (
        <div className="w-full h-full">
            {!newGroup &&
                (<div>
                    <div className="flex items-center space-x-10 bg-[#008069] text-white pt-16 px-10 pb-5">
                        <BsArrowLeft className="cursor-pointer text-2xl font-bold" />
                        <p className="text-xl font-semibold">
                            Add group participats
                        </p>
                    </div>
                    <div className="relative bg-white py-4 px-3">
                        <div className="flex space-x-2 flex-wrap space-y-1">
                            {groupMember.size > 0 && Array.from(groupMember).map((item) => (
                                <SelectedMember
                                    handleRemoveMember={() => handleRemoveMember(item)}
                                    member={item} />
                            ))}
                        </div>
                        <input className="outline-none border-b border-[#8888] p-2 w-[93%]" type="text" onChange={(e) => {
                            handleSearch(e.target.value)
                            setQuery(e.target.value)
                        }}
                            placeholder="Search user"
                            value={query} />
                    </div>
                    <div className="bg-white overflow-y-scroll h-[56.2vh] pr-8 pl-8">
                        {query && auth.searchUser?.map((item) =>
                            <div className="" onClick={() => {
                                groupMember.add(item)
                                setGroupMember(groupMember)
                                setQuery("");
                            }}
                                key={item?.id}>
                                <hr />
                                <ChatCard userImg={item.avatar} name={item.fullName} />
                            </div>
                        )}
                    </div>
                    <div className="bottom-10 py-10 bg-slate-300 flex items-center justify-center h-[17.7vh]">
                        <div className="bg-green-600 rounded-full p-4 cursor-pointer" onClick={() => {
                            setNewGroup(true)
                        }}>
                            <BsArrowRight className="text-white font-bold text-2xl cursor-pointer" />
                        </div>
                    </div>
                </div>)}
            {newGroup && <NewGroup setIsGroup={setIsGroup} groupMember={groupMember} />}
        </div>
    );
};

export default CreateGroup