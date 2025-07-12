import axios from "axios";

interface ChangePasswordPayload {
  email: string;
  currentPassword: string;
  newPassword: string;
}

export const changePassword = async (payload: ChangePasswordPayload) => {
  const res = await axios.post("/api/users/change-password", payload);
  return res.data;
};
