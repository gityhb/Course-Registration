"use client"

import { useState, useEffect, useCallback } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import axios from "axios";

//백엔드 주소
const API_URL = "https://sugang-api-429428399883.asia-northeast3.run.app"; 

const timeSlots = [
  "12:00", "12:25", "12:50", "13:15", "13:40", "14:05", "14:30", "15:00",
  "15:25", "15:50", "16:15", "16:40", "17:05", "17:30", "18:00", "18:25",
  "18:50", "19:20", "19:45", "20:10", "20:35", "21:00", "21:25", "21:50",
  "22:15", "22:40"
];

const tomorrow = new Date();
tomorrow.setDate(tomorrow.getDate() + 1);
const oneYearFromNow = new Date();
oneYearFromNow.setFullYear(oneYearFromNow.getFullYear() + 1);

const COURSE_INSTRUCTOR_MAP = {
    "201": 301, 
    "202": 302, 
    "203": 303  
};

function Registration() {
    const [startDate, setStartDate] = useState(null);
    const [selectedTime, setSelectedTime] = useState(null);
    const [bookedTimes, setBookedTimes] = useState([]);
    
    const [selectedCourse, setSelectedCourse] = useState("201");
    const [selectedDay, setSelectedDay] = useState(1); 

    const currentInstructorId = COURSE_INSTRUCTOR_MAP[selectedCourse];

    const fetchBookedTimes = useCallback(() => {
        if (!startDate) return;

        const year = startDate.getFullYear();
        const month = String(startDate.getMonth() + 1).padStart(2, "0");
        const day = String(startDate.getDate()).padStart(2, "0");
        const formattedDate = `${year}-${month}-${day}`;

        axios.get(`${API_URL}/api/sessions/booked-times`, {
            params: {
                date: formattedDate,
                instructorId: currentInstructorId
            }
        })
        .then(response => {
            const times = response.data.map(time => time.slice(0, 5));
            setBookedTimes(times);
            console.log(`Instructor ${currentInstructorId} booked times updated:`, times);
        })
        .catch(error => {
            console.error("Error fetching booked times:", error);
            setBookedTimes([]); 
        });
    }, [startDate, currentInstructorId]); 

    useEffect(() => {
        fetchBookedTimes();
    }, [fetchBookedTimes]);

    //날짜 필터링
    const isDateAvailable = (date) => {
        const day = date.getDay(); 
        if (selectedDay === 1) return day === 1 || day === 3 || day === 5;
        else return day === 2 || day === 4;
    };

    const handleDayChange = (e) => {
        setSelectedDay(Number(e.target.value));
        setStartDate(null);    
        setSelectedTime(null); 
    };

    //제출
    const handleSubmit = async () => {
        if (!startDate) {
            alert("수업 시작일을 선택해주세요.");
            return;
        }
        if (!selectedTime) {
            alert("수업시간을 선택해주세요.");
            return;
        }

        // 한국 시간 기준 날짜 생성 (저장용)
        const year = startDate.getFullYear();
        const month = String(startDate.getMonth() + 1).padStart(2, "0");
        const day = String(startDate.getDate()).padStart(2, "0");
        const localStartDate = `${year}-${month}-${day}`;

        const enrollmentData = {
            studentFk: 101, 
            courseFk: Number(selectedCourse),
            instructorFk: currentInstructorId,
            startDate: localStartDate, 
            classDay: selectedDay,
            startTime: `${selectedTime}:00`,
            duration: 20, 
            countSession: 32 
        };

        try {
            await axios.post(`${API_URL}/api/enrollments`, enrollmentData);
            
            alert("수강신청이 완료되었습니다!");
            
            // ★ 여기가 핵심! 새로고침(reload) 대신 상태만 초기화 ★
            setSelectedTime(null); // 1. 선택했던 시간만 지움 (날짜는 유지 -> 연속 신청 가능)
            fetchBookedTimes();    // 2. 예약된 시간 다시 불러옴 (방금 신청한 거 회색으로 변함)

        } catch (error) {
            console.error("Error creating enrollment:", error);
            alert("신청 중 오류가 발생했습니다.");
            fetchBookedTimes();
        }
    };

    return (
        <div className="container mx-auto px-40">
            <div className="h-32 pl-10 flex items-center border-b border-black">
                 <div className="text-3xl font-semibold ">화상영어 수강신청</div>
            </div>
            <div className="h-16 pt-5 pl-4 flex items-center border-b border-black">
                <div className="text-sm font-semibold text-blue-400">※ 첫 수업은 레벨테스트로 진행됩니다.</div>
            </div>

            {/* 과정 선택 */}
            <div className="h-24 flex items-center border-b border-black">
                <div className="h-full text-xl font-semibold border-r border-black flex items-center bg-gray-200 w-52 justify-center">
                    과정
                </div>
                <div className="text-xl font-semibold pt-3 px-5 flex items-center">
                    <div className="flex flex-col gap-1">
                        <select 
                            id="course" 
                            name="course" 
                            className="px-2 w-80 h-9 border border-gray"
                            value={selectedCourse} 
                            onChange={(e) => {
                                setSelectedCourse(e.target.value);
                                setSelectedTime(null); 
                            }} 
                        >
                            <option value="201">스마트파닉스</option>
                            <option value="202">여행영어</option>
                            <option value="203">프리토킹</option>
                        </select>
                        <div className="text-sm font-semibold text-blue-400 pt-1">
                            ※ 과목에 따라 담당 강사님이 배정됩니다.
                        </div>
                    </div>
                </div>
            </div>

            {/* 수업 요일 선택 */}
            <div className="h-20 flex items-center border-b border-black">
                <div className="h-full text-xl font-semibold border-r border-black flex items-center bg-gray-200 w-52 justify-center">
                    수업 요일
                </div>
                <div className="text-xl font-semibold px-5 flex items-center">
                    <select 
                        id="day" 
                        name="day" 
                        className="px-2 w-80 h-9 border border-gray"
                        value={selectedDay} 
                        onChange={handleDayChange}
                    >
                        <option value="1">월, 수, 금</option>
                        <option value="2">화, 목</option>
                    </select>
                </div>
            </div>

            {/* 수업 시작일 선택 */}
            <div className="h-20 flex items-center border-b border-black">
                <div className="h-full text-xl font-semibold border-r border-black flex items-center bg-gray-200 w-52 justify-center">
                    수업 시작일
                </div>
                <div className="text-xl font-semibold px-5 flex items-center">
                    <DatePicker
                        selected={startDate}
                        onChange={(date) => {
                            setStartDate(date);
                            setSelectedTime(null); 
                        }}
                        className="h-11 border border-gray px-3"
                        dateFormat="yyyy년 MM월 dd일"
                        minDate={tomorrow}
                        maxDate={oneYearFromNow}
                        filterDate={isDateAvailable} 
                        placeholderText="날짜를 선택하세요"
                    />
                </div>
            </div>

            {/* 수업시간 선택 */}
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
                                    ${isBooked ? 'text-gray-500 line-through cursor-not-allowed' : 'text-black-600'}
                                    ${isSelected ? 'bg-blue-600 text-white' : (isBooked ? 'bg-gray-300' : 'bg-white hover:bg-gray-100')}
                                `}
                            >
                                {time}
                            </button>
                        );
                    })}
                </div>
            </div>
            
            <div className="div h-36 flex items-center justify-center">
                <button 
                    className="bg-blue-600 text-white w-52 px-12 py-3 rounded-md text-lg font-semibold"
                    onClick={handleSubmit} 
                >
                    신 청
                </button>
                <button className="bg-gray-400 text-white w-52 px-6 py-3 rounded-md text-lg font-semibold border border-gray ml-16">
                    취 소
                </button>
            </div>
        </div>
    );
}

export default Registration;