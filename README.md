Java TPC-C
==========

This project is a Java implementation of the TPC-C benchmark.

========
Database
========

To create the tpcc schema in MySQL:

cd database
mysql -u root
> source create_tables.sql
> source add_fkey_idx.sql

It is possible to load data without the foreign keys and indexes in place and then add those
after loading data to improve loading times.

=================================
Generating and loading TPC-C data
=================================

Data can be loaded directly into a MySQL instance and can also be generated to CSV files that
can be loaded into MySQL later using LOAD DATA INFILE.

In tpcc.properties set the MODE to either CSV or JDBC.

To run the load process, run "ant tpccload".

It is possible to load data into shards where the warehouse ID is used as a shard key. The
SHARDCOUNT and SHARDID properties must be set correctly when generating or loading data.

This option requires the use of a JDBC driver that supports automatic sharding, such as
dbShards (http://www.dbshards.com).

===========================
Running the TPC-C Benchmark
===========================

To run the tpcc benchmarks, run "ant tpccrun".

Bugs can be reported to support@codefutures.com.

(c) 2013 CodeFutures Corporation.
