import { TypeOrmModule } from '@nestjs/typeorm';
import { DataSource } from 'typeorm';

// ruleid: missing-pool-configuration
export const unbounded = new DataSource({
  type: 'postgres',
  url: process.env.DATABASE_URL,
  entities: [],
});

// ruleid: missing-pool-configuration
export const nestUnbounded = TypeOrmModule.forRoot({
  type: 'postgres',
  url: process.env.DATABASE_URL,
  autoLoadEntities: true,
});

// ok: missing-pool-configuration
export const tuned = new DataSource({
  type: 'postgres',
  url: process.env.DATABASE_URL,
  poolSize: Number(process.env.DB_POOL_MAX ?? 10),
  entities: [],
});

// ok: missing-pool-configuration
export const tunedExtra = new DataSource({
  type: 'postgres',
  url: process.env.DATABASE_URL,
  extra: { max: 10, idleTimeoutMillis: 30_000 },
  entities: [],
});

// ok: missing-pool-configuration
export const nestTuned = TypeOrmModule.forRoot({
  type: 'postgres',
  url: process.env.DATABASE_URL,
  poolSize: 10,
  autoLoadEntities: true,
});
