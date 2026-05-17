export type NotmidSource = "fixture" | "api" | "cache";

export type NotmidMetricSet = {
  likes: number;
  saves: number;
  comments: number;
  distanceLabel: string;
};

export type NotmidPlace = {
  id: string;
  name: string;
  neighborhood: string;
  category: string;
  address: string;
  lat: number;
  lng: number;
  openNow: boolean;
  score: number;
  receiptCount: number;
  coverImageUrl: string;
};

export type NotmidClip = {
  id: string;
  title: string;
  caption: string;
  creatorHandle: string;
  placeId: string;
  moodTags: string[];
  capturedAtLabel: string;
  coverImageUrl: string;
  videoUrl?: string;
  metrics: NotmidMetricSet;
};

export type NotmidThread = {
  id: string;
  title: string;
  preview: string;
  updatedAtLabel: string;
  participantHandles: string[];
  attachedPlaceId?: string;
  attachedClipId?: string;
  unreadCount: number;
};

export type NotmidFeedResponse = {
  source: NotmidSource;
  generatedAt: string;
  clips: NotmidClip[];
  places: NotmidPlace[];
};

export type NotmidMapResponse = {
  source: NotmidSource;
  generatedAt: string;
  places: NotmidPlace[];
  highlightedClipIds: string[];
};

export type NotmidInboxResponse = {
  source: NotmidSource;
  generatedAt: string;
  threads: NotmidThread[];
};

export type NotmidAuthMode = "fake" | "firebase" | "disabled";

export type NotmidAuthProvider = "fake" | "anonymous" | "google";

export type NotmidAuthIntent = "browse" | "capture" | "chat" | "profile";

export type NotmidAuthRequirement = "capture" | "save" | "chat" | "profile-edit" | "moderation";

export type NotmidAuthUser = {
  id: string;
  handle: string;
  displayName: string;
  homeNeighborhood: string;
  avatarImageUrl: string;
  roles: string[];
};

export type NotmidAuthSession = {
  accessToken: string;
  provider: NotmidAuthProvider;
  expiresAt: string;
  user: NotmidAuthUser;
};

export type NotmidAuthStatusResponse = {
  source: NotmidSource;
  generatedAt: string;
  mode: NotmidAuthMode;
  authenticated: boolean;
  user?: NotmidAuthUser;
  sessionExpiresAt?: string;
  requiredFor: NotmidAuthRequirement[];
};

export type NotmidSignInRequest = {
  provider: NotmidAuthProvider;
  intent?: NotmidAuthIntent;
  returnTo?: string;
};

export type NotmidSignInResponse = {
  mode: NotmidAuthMode;
  session: NotmidAuthSession;
  nextPath: string;
};

export type NotmidErrorResponse = {
  error: {
    code: string;
    message: string;
  };
};
