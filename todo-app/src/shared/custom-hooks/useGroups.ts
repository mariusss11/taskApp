import GroupService from "../../services/group.service";
import { useQueryClient, useQuery } from "@tanstack/react-query";
import type { Group } from "../types/profile.types";
import useCheckAuth from "./useCheckAuth";

const useGroups = () => {
  const groupService = new GroupService();
  const auth = useCheckAuth();
  useQueryClient();
  const groupsQuery = useQuery({
    queryKey: ["groups"],
    queryFn: async () => await groupService.getGroups(),
    enabled: auth,
  });

  if (auth && groupsQuery.data) {
    const paths: Group[] = groupsQuery.data.map((item: Group) => {
      return {
        id: item.id,
        name: item.name,
      };
    });
    return paths;
  }
  return null;
};

export default useGroups;
