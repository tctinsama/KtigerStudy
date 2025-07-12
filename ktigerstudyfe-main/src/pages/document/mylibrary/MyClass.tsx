// src/pages/MyClass.tsx
import { JSX, useEffect, useState } from "react";
import { Link, useLocation, useNavigate, Outlet } from "react-router-dom";
import { FaTrash } from "react-icons/fa";
import PageMeta from "../../../components/document/common/PageMeta";
import { authService } from "../../../services/authService";

interface ClassResponse {
    classId: number;
    className: string;
    description?: string;
    createdAt: string;
}

const tabs = [
    { label: "Tài liệu", path: "/documents/Library/tai-lieu" },
    { label: "Tài liệu yêu thích", path: "/documents/Library/tailieuyeuthich" },
    { label: "Lớp học của tôi", path: "/documents/Library/lop-hoc" },
    { label: "Lớp học tham gia", path: "/documents/Library/lophocthamgia" },
];

export default function MyClass(): JSX.Element {
    const location = useLocation();
    const navigate = useNavigate();
    const [classes, setClasses] = useState<ClassResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const userId = authService.getUserId();
    const API_URL = import.meta.env.VITE_API_BASE_URL;

    // Fetch user's classes
    useEffect(() => {
        if (!userId) {
            setError("Bạn chưa đăng nhập.");
            setLoading(false);
            return;
        }
        (async () => {
            try {
                const res = await fetch(`${API_URL}/classes/user/${userId}`);
                if (!res.ok) throw new Error(`Lỗi ${res.status}`);
                setClasses(await res.json());
            } catch (e: any) {
                setError(e.message || "Không thể tải lớp học");
            } finally {
                setLoading(false);
            }
        })();
    }, [API_URL, userId]);

    // Delete a class
    const handleDelete = async (classId: number) => {
        if (!window.confirm("Bạn có chắc muốn xóa lớp này?")) return;
        try {
            const res = await fetch(`${API_URL}/classes/${classId}`, {
                method: "DELETE",
            });
            if (!res.ok) throw new Error(`Lỗi ${res.status}`);
            setClasses((prev) => prev.filter((c) => c.classId !== classId));
        } catch (e: any) {
            alert("Xóa không thành công: " + e.message);
        }
    };

    return (
        <>
            <PageMeta
                title="Lớp học | Thư viện của bạn"
                description="Danh sách lớp học của bạn"
            />

            {/* Header Tabs + Tạo lớp học */}
            <div className="bg-white dark:bg-zinc-900 rounded-xl shadow-sm p-4 mb-6 flex flex-wrap items-center justify-between">
                <nav className="flex space-x-4 text-sm font-medium text-gray-700 dark:text-gray-200">
                    {tabs.map((tab) => (
                        <button
                            key={tab.path}
                            onClick={() => navigate(tab.path)}
                            className={`py-2 px-4 rounded-lg transition-colors duration-200 ${location.pathname === tab.path
                                    ? "bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-200"
                                    : "hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-zinc-800 dark:hover:text-white"
                                }`}
                        >
                            {tab.label}
                        </button>
                    ))}
                </nav>
                <button
                    onClick={() => navigate("create")}
                    className="mt-2 sm:mt-0 bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition"
                >
                    + Tạo lớp học
                </button>
            </div>

            {/* Loading / Error */}
            {loading && (
                <p className="text-center dark:text-gray-200">Đang tải lớp học…</p>
            )}
            {error && (
                <p className="text-center text-red-600 dark:text-red-400">{error}</p>
            )}

            {/* Empty state */}
            {!loading && !error && classes.length === 0 && (
                <div className="flex flex-col items-center justify-center mt-16 space-y-4">
                    <p className="text-xl font-semibold text-gray-800 dark:text-gray-100 text-center">
                        Bạn chưa tạo hoặc tham gia lớp học nào
                    </p>
                    <button
                        onClick={() => navigate("create")}
                        className="bg-green-500 text-white px-6 py-2 rounded-lg hover:bg-green-600 transition"
                    >
                        + Tạo lớp học
                    </button>
                </div>
            )}

            {/* Class cards */}
            {!loading && !error && classes.length > 0 && (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                    {classes.map((cls) => (
                        <div key={cls.classId} className="relative">
                            <Link
                                to={`classes/${cls.classId}`}
                                className="block cursor-pointer bg-white dark:bg-zinc-800 rounded-xl shadow hover:shadow-lg transition p-6 space-y-3 border border-gray-100 dark:border-zinc-700 hover:border-blue-400 dark:hover:border-blue-400"
                                aria-label={`Xem chi tiết lớp học ${cls.className}`}
                            >
                                <h3 className="text-xl font-semibold text-gray-800 dark:text-gray-100">
                                    {cls.className}
                                </h3>
                                {cls.description && (
                                    <p className="text-gray-600 dark:text-gray-300 truncate">
                                        {cls.description}
                                    </p>
                                )}
                                <p className="text-gray-400 dark:text-gray-400 text-sm">
                                    Tạo ngày{" "}
                                    {new Date(cls.createdAt).toLocaleDateString("vi-VN", {
                                        day: "2-digit",
                                        month: "2-digit",
                                        year: "numeric",
                                    })}
                                </p>
                            </Link>

                            {/* Delete icon */}
                            <button
                                onClick={() => handleDelete(cls.classId)}
                                aria-label="Xóa lớp học"
                                className="absolute top-3 right-3 text-red-500 hover:text-red-700 p-1"
                            >
                                <FaTrash size={16} />
                            </button>
                        </div>
                    ))}
                </div>
            )}

            {/* render nested routes if any */}
            <Outlet />
        </>
    );
}
