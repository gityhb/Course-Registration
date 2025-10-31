"use client"

import { useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

const timeSlots = [
  "12:00", "12:25", "12:50", "13:15", "13:40", "14:05", "14:30", "15:00",
  "15:25", "15:50", "16:15", "16:40", "17:05", "17:30", "18:00", "18:25",
  "18:50", "19:20", "19:45", "20:10", "20:35", "21:00", "21:25", "21:50",
  "22:15", "22:40"
];

const bookedTimes = ["12:25", "17:05", "20:10", "22:40"];
//DB에서 저장된 시간들 (타인이 예약, 추후 수정)

function Registration(){
    const [startDate, setStartDate] = useState(new Date());
    const [selectedTime, setSelectedTime] = useState(null);

    return(
        <div className="container mx-auto">
            <div className="h-20 pl-4 flex items-center border-b border-black">
                <div className="text-2xl font-bold ">화상영어 수강신청</div>
            </div>

            <div className="h-16 pt-5 pl-4 flex items-center border-b border-black">
                <div className="text-sm font-semibold text-blue-400">※ 첫 수업은 레벨테스트로 진행됩니다.</div>
            </div>

            <div className="h-24 flex items-center border-b border-black">
                <div className="h-full text-xl font-semibold border-r border-black flex items-center bg-gray-200 w-52 justify-center">
                    과정
                </div>
                <div className="text-xl font-semibold pt-3 px-5 flex items-center">
                    <div className="flex flex-col gap-1">
                        <select id="course" name="course" className="px-2 w-80 h-9 border border-gray">
                            <option value="samrteng" selected>스마트파닉스</option>
                            <option value="traveleng">여행영어</option>
                            <option value="freeeng">프리토킹</option>
                        </select>
                        <div className="text-sm font-semibold text-blue-400 pt-1">
                            ※ 수업과정은 강사님의 추천과정에 의해 변경될 수 있습니다.
                        </div>
                    </div>
                </div>
            </div>

            <div className="h-20 flex items-center border-b border-black">
                <div className="h-full text-xl font-semibold border-r border-black flex items-center bg-gray-200 w-52 justify-center">
                    수업 요일
                </div>
                <div className="text-xl font-semibold px-5 flex items-center">
                    <select id="day" name="day" className="px-2 w-80 h-9 border border-gray">
                        <option value="mwfday" selected>월, 수, 금</option>
                        <option value="ttday">화, 목</option>
                    </select>
                </div>
            </div>

            <div className="h-20 flex items-center border-b border-black">
                <div className="h-full text-xl font-semibold border-r border-black flex items-center bg-gray-200 w-52 justify-center">
                    수업 시작일
                </div>
                <div className="text-xl font-semibold px-5 flex items-center">
                    <DatePicker
                        selected={startDate} 
                        onChange={(date) => setStartDate(date)} 
                        className="h-11 border border-gray px-3"
                        dateFormat="yyyy년 MM월 dd일"
                    />
                </div>
            </div>

            <div className="flex border-b border-black">
                <div className="text-xl font-semibold border-r border-black flex items-center bg-gray-200 w-52 justify-center">
                    수업시간 선택
                </div>
                <div className="flex-1 p-4 grid grid-cols-8 gap-2">
                    {timeSlots.map((time) => {
                        const isBooked = bookedTimes.includes(time);
                        const isSelected = selectedTime === time;

                        return (
                            <button
                                key={time}
                                disabled={isBooked}
                                onClick={() => setSelectedTime(time)}
                                className={`
                                    p-2 border rounded-md text-sm font-medium
                                    ${isBooked ? 'text-gray-400 line-through cursor-not-allowed' : 'text-orange-600'}
                                    ${isSelected ? 'bg-orange-500 text-white' : (isBooked ? 'bg-gray-200' : 'bg-white hover:bg-orange-100')}
                                `}
                            >
                                {time}
                            </button>
                        );
                    })}
                </div>
            </div>

        </div>
    );
}

export default Registration;